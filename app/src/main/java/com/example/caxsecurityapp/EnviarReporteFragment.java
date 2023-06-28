package com.example.caxsecurityapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationHolder;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.custom.CustomErrorReset;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidation;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidationCallback;
import com.example.caxsecurityapp.entities.Barrios;
import com.example.caxsecurityapp.entities.Delitos;
import com.example.caxsecurityapp.entities.EstadoBarrio;
import com.example.caxsecurityapp.entities.Reportes;
import com.example.caxsecurityapp.entities.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EnviarReporteFragment extends Fragment {

    private Spinner spTipoReporte, spNombreBarrio;
    private RadioButton rbReporteAnonimo, rbReportePublico;
    private TextInputEditText tieDescripcionReporte, tieDireccionReporte, tieReferenciaReporte;
    DatabaseReference mRootReference;
    public String nombreBarrio = "", nombreDelito = "";
    public String idBarrio = "", idDelito = "";
    private Button btnSendReport, btnTomarFotoRep, btnGrabarVideoRep, btnSeleccionarFotoRep, btnSeleccionarVideoRep, btnEliminarArchivoRep;
    private FirebaseAuth mAuth;
    private ImageView ivImagenReporte;
    private VideoView vvVideoReporte;
    private MediaController mc;
    private Uri multimedia;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    String storage_path = "reportes/", linkMultimedia = "";
    public EstadoBarrio estadoBarrio = new EstadoBarrio("",0,0,0);
    public String tipoMultimedia = "vacio";
    public Usuario user;
    AwesomeValidation awesomeValidation;

    static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 1001;
    private static final int REQUEST_GALLERY_VIDEO_PERMISSION = 1002;
    private static final int REQUEST_PERMISSIONS = 4;

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enviar_reporte, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spTipoReporte = view.findViewById(R.id.spTipoReporteFragment);
        spNombreBarrio = view.findViewById(R.id.spNombreBarrioFragment);
        tieDescripcionReporte = view.findViewById(R.id.tieDescripcionReporte);
        tieDireccionReporte = view.findViewById(R.id.tieDireccionReporte);
        tieReferenciaReporte = view.findViewById(R.id.tieReferenciaReporte);
        rbReporteAnonimo = view.findViewById(R.id.rbReporteAnonimo);
        rbReportePublico = view.findViewById(R.id.rbReportePublico);
        mRootReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this.getContext());
        btnSendReport = view.findViewById(R.id.btnSendReport);
        btnTomarFotoRep = view.findViewById(R.id.btnTomarFotoRep);
        btnGrabarVideoRep = view.findViewById(R.id.btnGrabarVideoRep);
        btnSeleccionarFotoRep = view.findViewById(R.id.btnSeleccionarFotoRep);
        btnSeleccionarVideoRep = view.findViewById(R.id.btnSeleccionarVideoRep);
        btnEliminarArchivoRep = view.findViewById(R.id.btnEliminarArchivoRep);
        ivImagenReporte = view.findViewById(R.id.ivImagenReporte);
        vvVideoReporte = view.findViewById(R.id.vvVideoReporte);

        storage_path += "/" + mAuth.getUid() + "/*";

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this.getActivity(), R.id.tieDescripcionReporte, ".{4,}", R.string.invalid_descripcion);
        awesomeValidation.addValidation(this.getActivity(), R.id.tieDireccionReporte, ".{5,}", R.string.invalid_direccion);
        awesomeValidation.addValidation(this.getActivity(), R.id.tieReferenciaReporte, ".{3,}", R.string.invalid_direccion);
        awesomeValidation.addValidation(this.getActivity(), R.id.spTipoReporteFragment, new CustomValidation() {
            @Override
            public boolean compare(ValidationHolder validationHolder) {
                if (((Spinner) validationHolder.getView()).getSelectedItem().toString().equals("-- SELECCIONA DELITO --")) {
                    return false;
                } else {
                    return true;
                }
            }
        }, new CustomValidationCallback() {
            @Override
            public void execute(ValidationHolder validationHolder) {
                TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
                textViewError.setError(validationHolder.getErrMsg());
                textViewError.setTextColor(Color.RED);
            }
        }, new CustomErrorReset() {
            @Override
            public void reset(ValidationHolder validationHolder) {
                TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
                textViewError.setError(null);
                textViewError.setTextColor(Color.BLACK);
            }
        }, R.string.invalid_sp_delito);

        awesomeValidation.addValidation(this.getActivity(), R.id.spNombreBarrioFragment, new CustomValidation() {
            @Override
            public boolean compare(ValidationHolder validationHolder) {
                if (((Spinner) validationHolder.getView()).getSelectedItem().toString().equals("-- SELECCIONA BARRIO --")) {
                    return false;
                } else {
                    return true;
                }
            }
        }, new CustomValidationCallback() {
            @Override
            public void execute(ValidationHolder validationHolder) {
                TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
                textViewError.setError(validationHolder.getErrMsg());
                textViewError.setTextColor(Color.RED);
            }
        }, new CustomErrorReset() {
            @Override
            public void reset(ValidationHolder validationHolder) {
                TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
                textViewError.setError(null);
                textViewError.setTextColor(Color.BLACK);
            }
        }, R.string.invalid_sp_barrio);

        mc = new MediaController(EnviarReporteFragment.this.getContext());

        estadoBarrio = new EstadoBarrio("",0,0,0);

        limpiarIV();

        obtenerDatosUsuario();

        obtenerDelitos();
        obtenerBarrios();

        btnTomarFotoRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
                vvVideoReporte.setVisibility(View.GONE);
                ivImagenReporte.setVisibility(View.VISIBLE);
            }
        });

        btnGrabarVideoRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraVideoPermission();
                vvVideoReporte.setVisibility(View.VISIBLE);
                ivImagenReporte.setVisibility(View.GONE);
                vvVideoReporte.setMediaController(mc);
                mc.setAnchorView(vvVideoReporte);
            }
        });

        btnSeleccionarFotoRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGaleriaFoto();
            }
        });

        btnSeleccionarVideoRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGaleriaVideo();
            }
        });

        btnEliminarArchivoRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multimedia = Uri.parse("");
                limpiarIV();
                Toast.makeText(view.getContext(), "Archivo quitado", Toast.LENGTH_SHORT).show();
            }
        });

        btnSendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Enviando reporte");
                progressDialog.show();
                DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
                String date = dateFormat.format(Calendar.getInstance().getTime());
                DateFormat dateFormat2 = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
                String date2 = dateFormat2.format(Calendar.getInstance().getTime());
                obtenerEstadoBarrio(date);
                String descripcionReporte = tieDescripcionReporte.getText().toString();
                String direccion = tieDireccionReporte.getText().toString();
                String referencia = tieReferenciaReporte.getText().toString();
                if(awesomeValidation.validate()){
                    if(user.baneado == 0){
                        if(rbReporteAnonimo.isChecked()){
                            cargarDatosFirebase(descripcionReporte, direccion, referencia, date2);
                            cargarReferenciaDataReportes(date2);
                        }
                        else if(rbReportePublico.isChecked()){
                            int d1 = estadoBarrio.getD1();
                            int d2 = estadoBarrio.getD2();
                            int d3 = estadoBarrio.getD3();
                            if(estadoBarrio.getId() != ""){
                                switch (idDelito){
                                    case "1": d1 += 1; break;
                                    case "2": d2 += 1; break;
                                    case "3": d3 += 1; break;
                                }
                                actualizarReporteBarrioFirebase(date, d1, d2 , d3);
                            }
                            else {
                                switch (idDelito){
                                    case "1": d1 += 1; break;
                                    case "2": d2 += 1; break;
                                    case "3": d3 += 1; break;
                                }
                                cargarReporteBarrioFirebase(date, d1, d2 , d3);
                            }
                            cargarDatosFirebase(descripcionReporte, direccion, referencia, date2);
                            cargarReferenciaDataReportes(date2);
                            estadoBarrio = new EstadoBarrio("",0,0,0);
                            limpiarFragmente();
                        }
                        else {
                            Toast.makeText(EnviarReporteFragment.this.getContext(), "Debe seleccionar el tipo de reporte", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                    else {
                        Toast.makeText(EnviarReporteFragment.this.getContext(), "Usuario baneado", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
                else {
                    Toast.makeText(EnviarReporteFragment.this.getContext(), "Complete los campos requeridos", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void limpiarFragmente(){
        tieDescripcionReporte.setText("");
        tieDireccionReporte.setText("");
        tieReferenciaReporte.setText("");
        spTipoReporte.setSelection(0);
        spNombreBarrio.setSelection(0);
        rbReporteAnonimo.setSelected(false);
        rbReportePublico.setSelected(false);
        limpiarIV();
        estadoBarrio = new EstadoBarrio("",0,0,0);
    }

    private void cargarReferenciaDataReportes(String idReporte) {
        Map<String, Object> dataReporte = new HashMap<>();
        dataReporte.put("idReporte", idReporte);
        dataReporte.put("idUsuario", mAuth.getUid());

        mRootReference.child("IndicesReportes/Enviados/").push().setValue(dataReporte);
    }

    private void cargarReporteBarrioFirebase(String date, int d1, int d2, int d3) {
        Map<String, Object> reporteBarrio = new HashMap<>();
        reporteBarrio.put("d1", d1);
        reporteBarrio.put("d2", d2);
        reporteBarrio.put("d3", d3);

        mRootReference.child("EstadoBarrios/"+date+"/"+idBarrio).setValue(reporteBarrio);
    }

    private void actualizarReporteBarrioFirebase(String date, int d1, int d2, int d3) {
        Map<String, Object> reporteBarrio = new HashMap<>();
        reporteBarrio.put("d1", d1);
        reporteBarrio.put("d2", d2);
        reporteBarrio.put("d3", d3);

        mRootReference.child("EstadoBarrios/"+date+"/"+ idBarrio).updateChildren(reporteBarrio);
    }

    public void obtenerEstadoBarrio(String fecha){
        mRootReference.child("EstadoBarrios/"+fecha+"/"+idBarrio).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                estadoBarrio = dataSnapshot.getValue(EstadoBarrio.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Estado barrio", "No se han hecho reportes en el barrio");
            }
        });
    }

    public void limpiarIV(){
        vvVideoReporte.setVisibility(View.GONE);
        ivImagenReporte.setVisibility(View.VISIBLE);

        ivImagenReporte.setImageResource(R.drawable.ic_image);

        btnEliminarArchivoRep.setEnabled(false);
    }

    private void iniciarGrabacionVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        videoLauncher.launch(takeVideoIntent);
    }

    private void checkCameraVideoPermission() {
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_VIDEO_CAPTURE);
        } else {
            iniciarGrabacionVideo();
        }
    }

    private ActivityResultLauncher<Intent> videoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    vvVideoReporte.setVisibility(View.VISIBLE);
                    ivImagenReporte.setVisibility(View.GONE);

                    multimedia = data.getData();
                    vvVideoReporte.setVideoURI(multimedia);

                    tipoMultimedia = "video";

                    btnEliminarArchivoRep.setEnabled(true);
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    Toast.makeText(requireContext(), "Grabación de video cancelada", Toast.LENGTH_LONG).show();
                }
            });

    public void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        ivImagenReporte.setImageBitmap(imageBitmap);
                        btnEliminarArchivoRep.setEnabled(true);

                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        String path = MediaStore.Images.Media.insertImage(EnviarReporteFragment.this.getActivity().getContentResolver(), imageBitmap, "Title", null);
                        multimedia = Uri.parse(path);

                        tipoMultimedia = "imagen";

                        btnEliminarArchivoRep.setEnabled(true);
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Toast.makeText(EnviarReporteFragment.this.getContext(), "Captura de foto cancelada", Toast.LENGTH_LONG).show();
                    }
                }
            });

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            abrirCamara();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamara();
            } else {
                Toast.makeText(EnviarReporteFragment.this.getContext(), "No se puede usar la cámara sin los permisos", Toast.LENGTH_LONG).show();
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
                abrirGaleriaFoto();
            } else {
                Toast.makeText(this.getContext(), "No se puede abrir la galería sin los permisos", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == REQUEST_GALLERY_VIDEO_PERMISSION) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                abrirGaleriaVideo();
            } else {
                Toast.makeText(this.getContext(), "No se puede abrir la galería sin los permisos", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == REQUEST_VIDEO_CAPTURE) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                iniciarGrabacionVideo();
            } else {
                Toast.makeText(requireContext(), "No se pueden grabar videos sin los permisos necesarios", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void abrirGaleriaFoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri imageUrl = data.getData();
                        if (imageUrl != null) {
                            vvVideoReporte.setVisibility(View.GONE);
                            ivImagenReporte.setVisibility(View.VISIBLE);
                            ivImagenReporte.setImageURI(imageUrl);
                            multimedia = imageUrl;
                            tipoMultimedia = "imagen";
                            btnEliminarArchivoRep.setEnabled(true);
                        }
                    }
                }
            }
    );

    public void abrirGaleriaVideo(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        someActivityResultLauncherVideo.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncherVideo = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri imageUrl = data.getData();
                        if (imageUrl != null) {
                            vvVideoReporte.setVisibility(View.VISIBLE);
                            ivImagenReporte.setVisibility(View.GONE);
                            vvVideoReporte.setMediaController(mc);
                            multimedia = data.getData();
                            vvVideoReporte.setVideoURI(multimedia);
                            tipoMultimedia = "video";
                            btnEliminarArchivoRep.setEnabled(true);
                        }
                    }
                }
            }
    );

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            vvVideoReporte.setVisibility(View.VISIBLE);
            ivImagenReporte.setVisibility(View.GONE);

            multimedia = data.getData();
            vvVideoReporte.setVideoURI(multimedia);

            tipoMultimedia = "video";

            btnEliminarArchivoRep.setEnabled(true);
        }
    }

    public void obtenerBarrios(){
        progressDialog.setMessage("Cargando barrios");
        progressDialog.show();
        final List<Barrios> barrios = new ArrayList<>();
        mRootReference.child("Barrios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String id = ds.getKey();
                        String nombre = ds.child("nombre").getValue().toString();
                        barrios.add(new Barrios(id, nombre));
                    }

                    ArrayAdapter<Barrios> arrayAdapter = new ArrayAdapter<>(EnviarReporteFragment.this.getContext(), android.R.layout.simple_spinner_dropdown_item, barrios);
                    spNombreBarrio.setAdapter(arrayAdapter);
                    spNombreBarrio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            nombreBarrio = adapterView.getItemAtPosition(i).toString();
                            idBarrio = "" + i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            progressDialog.dismiss();
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

    public void obtenerDelitos(){
        progressDialog.setMessage("Cargando delitos");
        progressDialog.show();
        final List<Delitos> delitos = new ArrayList<>();
        mRootReference.child("Delitos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String id = ds.getKey();
                        String nombreDelito = ds.child("nombreDelito").getValue().toString();
                        delitos.add(new Delitos(id, nombreDelito));
                    }

                    ArrayAdapter<Delitos> arrayAdapter = new ArrayAdapter<>(EnviarReporteFragment.this.getContext(), android.R.layout.simple_spinner_dropdown_item, delitos);
                    spTipoReporte.setAdapter(arrayAdapter);
                    spTipoReporte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            nombreDelito = adapterView.getItemAtPosition(i).toString();
                            idDelito = "" + i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }

    private void cargarDatosFirebase(String descripcionReporte, String direccion, String referencia, String date2) {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
        String date = dateFormat.format(Calendar.getInstance().getTime());

        if(multimedia!=null){
            subirDataYMultimedia(date2, descripcionReporte, direccion, referencia, date, nombreDelito, nombreBarrio);
        }
        else {
            Map<String, Object> datosReporte = new HashMap<>();
            datosReporte.put("tipoReporte", nombreDelito);
            datosReporte.put("fecha", date);
            datosReporte.put("ip", getIP());
            datosReporte.put("descripcionReporte", descripcionReporte);
            datosReporte.put("nombreBarrio", nombreBarrio);
            datosReporte.put("direccion", direccion);
            datosReporte.put("referencia", referencia);
            datosReporte.put("linkMultimedia", "");
            datosReporte.put("estado", "ENVIADO");
            datosReporte.put("tipoMultimedia", tipoMultimedia);

            mRootReference.child("Reportes/"+mAuth.getUid()+"/"+date2).setValue(datosReporte);
            Toast.makeText(EnviarReporteFragment.this.getContext(), "Reporte enviado", Toast.LENGTH_SHORT).show();

            tipoMultimedia = "vacio";
            progressDialog.dismiss();

        }
    }


    public void subirDataYMultimedia(String fecha, String descripcionReporte, String direccion, String referencia, String date, String delito, String barrio){
        String rute_storage_photo = storage_path + "multimediaReporte" + mAuth.getUid()+fecha;
        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(multimedia).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                if(uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            linkMultimedia = uri.toString();
                            Map<String, Object> datosReporte = new HashMap<>();
                            datosReporte.put("tipoReporte", delito);
                            datosReporte.put("fecha", date);
                            datosReporte.put("ip", getIP());
                            datosReporte.put("descripcionReporte", descripcionReporte);
                            datosReporte.put("nombreBarrio", barrio);
                            datosReporte.put("direccion", direccion);
                            datosReporte.put("referencia", referencia);
                            datosReporte.put("linkMultimedia", linkMultimedia);
                            datosReporte.put("estado", "ENVIADO");
                            datosReporte.put("tipoMultimedia", tipoMultimedia);

                            mRootReference.child("Reportes/"+mAuth.getUid()+"/"+fecha).setValue(datosReporte);
                            Toast.makeText(EnviarReporteFragment.this.getContext(), "Multimedia subido", Toast.LENGTH_SHORT).show();

                            tipoMultimedia = "vacio";
                            progressDialog.dismiss();
                            Toast.makeText(EnviarReporteFragment.this.getContext(), "Reporte enviado", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(EnviarReporteFragment.this.getContext(), "Error al cargar multimedia", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String getIP(){
        List<InetAddress> addrs;
        String address = "";
        try{
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for(NetworkInterface intf : interfaces){
                addrs = Collections.list(intf.getInetAddresses());
                for(InetAddress addr : addrs){
                    if(!addr.isLoopbackAddress() && addr instanceof Inet4Address){
                        address = addr.getHostAddress().toUpperCase(new Locale("es", "PE"));
                    }
                }
            }
        }catch (Exception e){
            Log.w("TAG", "Ex getting IP value " + e.getMessage());
        }
        return address;
    }

    public void obtenerDatosUsuario(){
        progressDialog.setMessage("Cargando datos usuario");
        progressDialog.show();
        mRootReference.child("Usuario/"+mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(Usuario.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EnviarReporteFragment.this.getContext(), "Error con la base de datos, contactese con soporte técnico", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }
}