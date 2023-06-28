package com.example.caxsecurityapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caxsecurityapp.entities.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MiPerfilActivity extends AppCompatActivity {

    DatabaseReference mRootReference;
    private TextView tvNombreUsuarioPerfil, tvDNIUsuarioPerfil, tvTelefonoUsuarioPerfil, tvCorreoUsuarioPerfil;
    private ImageButton btnEditNombreUsuario, btnEditDNIUsuario, btnEditTelefonoUsuario;
    private ImageView ivFotoPerfil;
    private Button btnTomarFoto, brnSeleccionarFoto, btnDeletePhoto;

    StorageReference storageReference;
    String storage_path = "fotosPerfil/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri imageUrl;
    String photo = "photo";
    String linkFoto = "", tempNombreFoto = "";

    ProgressDialog progressDialog;

    private static final int REQUEST_CAMERA_PERMISSION = 1001;
    private static final int REQUEST_CODE_PERMISSION = 102;
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;

    private static final int REQUEST_PERMISSIONS = 4;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_CAMERA = 3;

    private FirebaseAuth mAuth;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        mRootReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        tvNombreUsuarioPerfil = findViewById(R.id.tvNombreUsuarioPerfil);
        tvDNIUsuarioPerfil = findViewById(R.id.tvDNIUsuarioPerfil);
        tvTelefonoUsuarioPerfil = findViewById(R.id.tvTelefonoUsuarioPerfil);
        tvCorreoUsuarioPerfil = findViewById(R.id.tvCorreoUsuarioPerfil);

        btnEditNombreUsuario = findViewById(R.id.btnEditNombreUsuario);
        btnEditDNIUsuario = findViewById(R.id.btnEditDNIUsuario);
        btnEditTelefonoUsuario = findViewById(R.id.btnEditTelefonoUsuario);

        ivFotoPerfil = findViewById(R.id.ivFotoPerfil);
        brnSeleccionarFoto = findViewById(R.id.brnSeleccionarFoto);
        btnTomarFoto = findViewById(R.id.btnTomarFoto);
        btnDeletePhoto = findViewById(R.id.btnDeletePhoto);

        obtenerDatosUsuario();

        btnEditNombreUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditarNombreUsuarioFragment fm = new EditarNombreUsuarioFragment();
                fm.show(getSupportFragmentManager(), "Editar nombre usuario");
            }
        });

       btnEditDNIUsuario.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               EditarDniUsuarioFragment fm = new EditarDniUsuarioFragment();
               fm.show(getSupportFragmentManager(), "Editar DNI usuario");
           }
       });

       btnEditTelefonoUsuario.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               EditarTelefonoUsuarioFragment fm = new EditarTelefonoUsuarioFragment();
               fm.show(getSupportFragmentManager(), "Editar Teléfono usuario");
           }
       });

       btnTomarFoto.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               checkCameraPermission();
           }
       });

       brnSeleccionarFoto.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               uploadPhoto();
           }
       });

       btnDeletePhoto.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               progressDialog.setMessage("Eliminando foto");
               progressDialog.show();
               eliminarFotoFirestore();
           }
       });
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            abrirCamara();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamara();
            } else {
                Toast.makeText(MiPerfilActivity.this, "No se puede usar la cámara sin los permisos", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == REQUEST_PERMISSIONS) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                uploadPhoto();
            } else {
                Toast.makeText(MiPerfilActivity.this, "No se puede abrir la galería sin los permisos", Toast.LENGTH_LONG).show();
            }
        }
    }

    private ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        String path = MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, "Title", null);
                        imageUrl = Uri.parse(path);
                        subirPhoto(imageUrl);
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Toast.makeText(MiPerfilActivity.this, "Captura de foto cancelada", Toast.LENGTH_LONG).show();
                    }
                }
            });

    public void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(intent);
    }

    public void eliminarFotoFirestore(){
        storageReference.child("fotosPerfil/"+"*photo"+mAuth.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                btnDeletePhoto.setEnabled(true);
                eliminarFotoUsuario();
                Toast.makeText(MiPerfilActivity.this, "Foto eliminada", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(MiPerfilActivity.this, "Foto no eliminada, ocurrio un error", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    public void eliminarFotoUsuario(){
        Map<String, Object> eliminarFoto = new HashMap<>();
        eliminarFoto.put("photo", "");
        mRootReference.child("Usuario/"+mAuth.getUid()).updateChildren(eliminarFoto).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MiPerfilActivity.this, "Error al eliminar foto", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    public void irActualizarContrasena(View view){
        Intent intent = new Intent(getApplicationContext(), ActualizarContrasenaActivity.class);
        startActivity(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri imageUrl = data.getData();
                        if (imageUrl != null) {
                            subirPhoto(imageUrl);
                        }
                    }
                }
            }
    );

    private void uploadPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        someActivityResultLauncher.launch(intent);
    }

    private void subirPhoto(Uri imageUrl){
       progressDialog.setMessage("Subiendo foto");
       progressDialog.show();
       String rute_storage_photo = storage_path + "" + photo + "" + mAuth.getUid();
       StorageReference reference = storageReference.child(rute_storage_photo);
       reference.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
               while (!uriTask.isSuccessful());
                if(uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String dowloadUri = uri.toString();
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("photo", dowloadUri);
                            mRootReference.child("Usuario/"+mAuth.getUid()).updateChildren(map);
                            Toast.makeText(MiPerfilActivity.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                            btnDeletePhoto.setEnabled(true);
                            progressDialog.dismiss();
                        }
                    });
                }
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               progressDialog.dismiss();
               Toast.makeText(MiPerfilActivity.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
           }
       });
    }

    public void obtenerDatosUsuario(){
        progressDialog.setMessage("Cargando datos usuario");
        progressDialog.show();
        mRootReference.child("Usuario/"+mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario user = dataSnapshot.getValue(Usuario.class);
                tvNombreUsuarioPerfil.setText(user.nombre);
                tvDNIUsuarioPerfil.setText(user.dni);
                tvTelefonoUsuarioPerfil.setText(user.telefono);
                tvCorreoUsuarioPerfil.setText(user.correo);
                linkFoto = user.photo;
                if(linkFoto.length()<=0){
                    btnDeletePhoto.setEnabled(false);
                }
                else{
                    btnDeletePhoto.setEnabled(true);
                }
                try {
                    if(!linkFoto.equals("")){
                        Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 200);
                        toast.show();
                        Picasso.with(MiPerfilActivity.this)
                                .load(linkFoto)
                                .resize(120, 120)
                                .into(ivFotoPerfil);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "No se tiene una foto de perfil", Toast.LENGTH_LONG).show();
                        ivFotoPerfil.setImageResource(R.drawable.ic_perfil);
                    }
                }
                catch (Exception e){
                    Log.v("Error", "e: "+e);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error con la base de datos, contactese con soporte técnico", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(getApplicationContext(), InicioActivity.class);
        startActivity(intent);
        return super.onKeyDown(keyCode, event);
    }
}