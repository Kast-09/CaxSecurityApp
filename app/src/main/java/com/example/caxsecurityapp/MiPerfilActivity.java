package com.example.caxsecurityapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import java.util.HashMap;
import java.util.Map;

public class MiPerfilActivity extends AppCompatActivity {

    DatabaseReference mRootReference;
    private String email;
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
    String idUser, linkFoto, tempNombreFoto = "";

    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    Drawable drawable;

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

        obtenerCorreoUsuario();
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
               //int permiso = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.class.);
               /*if(checkSelfPermission(Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION()) == PackageManager.PERMISSION_GRANTED){

               }*/
               //abrirCamara();
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

    public void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//con esto es lo mínimo necesario para abrir la cámara
        startActivityForResult(intent, 1000);//se le pone cualquier número, sirve como código de respeusta
    }

    public void eliminarFotoFirestore(){
        storageReference.child("fotosPerfil/"+tempNombreFoto).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
            }
        });
    }

    public void eliminarFotoUsuario(){
        Map<String, Object> eliminarFoto = new HashMap<>();
        eliminarFoto.put("photo", "");
        Log.i("ID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        mRootReference.child("Usuario").child(idUser).updateChildren(eliminarFoto).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void obtenerCorreoUsuario(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            Log.i("ID", email);
        }
    }

    private void uploadPhoto(){
       Intent i = new Intent(Intent.ACTION_PICK);
       i.setType("image/*");
       startActivityForResult(i, COD_SEL_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("image_url", "requestCode - RESULT_OK: "+requestCode+" "+RESULT_OK);
        if(resultCode == RESULT_OK){
            if(requestCode == COD_SEL_IMAGE){
                imageUrl = data.getData();
                subirPhoto(imageUrl);
            }
        }
        if(requestCode == 1000 && resultCode == RESULT_OK){// el CAMERA_REQUEST es para validar que sea una petición de abrir la cámara y el RESULT_OK es para validar que al abrir la cámara todo salio bien y no hubo errores
            imageUrl = data.getData();
            subirPhoto(imageUrl);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirPhoto(Uri imageUrl){
       progressDialog.setMessage("Subiendo foto");
       progressDialog.show();
       String rute_storage_photo = storage_path + "" + photo + "" + mAuth.getUid() + ""+idUser;
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
                            mRootReference.child("Usuario").child(idUser).updateChildren(map);
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
               Toast.makeText(MiPerfilActivity.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
           }
       });
    }

    public void obtenerDatosUsuario(){
        progressDialog.setMessage("Cargando datos usuario");
        progressDialog.show();
        mRootReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    mRootReference.child("Usuario").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Usuario user = snapshot.getValue(Usuario.class);
                            if(user.correo.equalsIgnoreCase(email)){
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
                                    obtenerNombreImagen(linkFoto);
                                }
                                idUser = snapshot.getKey();
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
                                        Picasso.with(MiPerfilActivity.this)
                                                .load("https://firebasestorage.googleapis.com/v0/b/caxsecurity.appspot.com/o/fotosPerfil%2Fic_user_default.png?alt=media&token=89b7e229-0e17-467a-b94c-287e28347310")
                                                .resize(120, 120)
                                                .into(ivFotoPerfil);
                                    }
                                }
                                catch (Exception e){
                                    Log.v("Error", "e: "+e);
                                }
                            }
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Error al cargar información", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }

    public void obtenerNombreImagen(String link){
        char[] chars = new char[linkFoto.length()];
        int cont = 0;
        for(int i = 84;i<linkFoto.length();i++){
            if(linkFoto.charAt(i) != 63){
                tempNombreFoto += linkFoto.charAt(i);
                cont++;
            }
            else {
                break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(getApplicationContext(), InicioActivity.class);
        startActivity(intent);
        return super.onKeyDown(keyCode, event);
    }
}