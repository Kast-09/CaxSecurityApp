package com.example.caxsecurityapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;
import android.widget.VideoView;

import com.example.caxsecurityapp.entities.Barrios;
import com.example.caxsecurityapp.entities.Delitos;
import com.example.caxsecurityapp.entities.EstadoBarrio;
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
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private VideoView vvVideoReporte;
    private MediaController mc;
    private Uri multimedia;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    String storage_path = "reportes/", linkMultimedia = "";
    public EstadoBarrio estadoBarrio = new EstadoBarrio("",0,0,0);

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

        mc = new MediaController(EnviarReporteFragment.this.getContext());

        limpiarIV();

        obtenerDelitos();
        obtenerBarrios();

        btnTomarFotoRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(ContextCompat.checkSelfPermission(EnviarReporteFragment.this.getContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        if(ContextCompat.checkSelfPermission(EnviarReporteFragment.this.getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                            abrirCamara();
                            vvVideoReporte.setVisibility(View.GONE);
                            ivImagenReporte.setVisibility(View.VISIBLE);
                        }
                        else{
                            requestPermissions(new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                        }
                    }
                    else {
                        requestPermissions(new String[] {Manifest.permission.CAMERA}, 100);
                    }
                }
            }
        });

        btnGrabarVideoRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(EnviarReporteFragment.this.getContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(EnviarReporteFragment.this.getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        grabarVideo();
                        vvVideoReporte.setVisibility(View.VISIBLE);
                        ivImagenReporte.setVisibility(View.GONE);
                        vvVideoReporte.setMediaController(mc);
                        mc.setAnchorView(vvVideoReporte);
                    }
                    else{
                        requestPermissions(new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                    }
                }
                else {
                    requestPermissions(new String[] {Manifest.permission.CAMERA}, 100);
                }
            }
        });

        btnSeleccionarFotoRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(EnviarReporteFragment.this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    abrirGaleriaFoto();
                }
                else{
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
                }
            }
        });

        btnSeleccionarVideoRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(EnviarReporteFragment.this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    abrirGaleriaVideo();
                }
                else{
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
                }
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
                String reporte = spTipoReporte.getSelectedItem().toString();
                String descripcionReporte = tieDescripcionReporte.getText().toString();
                String direccion = tieDireccionReporte.getText().toString();
                String referencia = tieReferenciaReporte.getText().toString();
                if(idDelito != "0"){
                    if(idBarrio != "0"){
                        if(rbReporteAnonimo.isChecked()){
                            cargarDatosFirebase(reporte, descripcionReporte, direccion, referencia, date2);
                            cargarReferenciaDataReportes(date, date2);
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
                            cargarDatosFirebase(reporte, descripcionReporte, direccion, referencia, date2);
                            cargarReferenciaDataReportes(date, date2);
                            estadoBarrio = new EstadoBarrio("",0,0,0);
                            limpiarFragmente();
                        }
                        else {
                            Toast.makeText(EnviarReporteFragment.this.getContext(), "Debe seleccionar el tipo de reporte", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(EnviarReporteFragment.this.getContext(), "Debe seleccionar el tipo de barrio", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(EnviarReporteFragment.this.getContext(), "Debe seleccionar el tipo de delito", Toast.LENGTH_SHORT).show();
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

    private void cargarReferenciaDataReportes(String date, String idReporte) {
        Map<String, Object> dataReporte = new HashMap<>();
        dataReporte.put("idReporte", idReporte);
        dataReporte.put("idUsuario", mAuth.getUid());

        mRootReference.child("DataReportes/"+date).push().setValue(dataReporte);
    }

    private void cargarReporteBarrioFirebase(String date, int d1, int d2, int d3) {
        Map<String, Object> reporteBarrio = new HashMap<>();
        reporteBarrio.put("d1", d1);
        reporteBarrio.put("d2", d2);
        reporteBarrio.put("d3", d3);

        mRootReference.child("EstadoBarrios/"+date+"/"+ idBarrio).setValue(reporteBarrio);
    }

    private void actualizarReporteBarrioFirebase(String date, int d1, int d2, int d3) {
        Map<String, Object> reporteBarrio = new HashMap<>();
        reporteBarrio.put("d1", d1);
        reporteBarrio.put("d2", d2);
        reporteBarrio.put("d3", d3);

        mRootReference.child("EstadoBarrios/"+date+"/"+ idBarrio).updateChildren(reporteBarrio);
    }

    public void obtenerEstadoBarrio(String fecha){
        mRootReference.child("EstadoBarrios/"+fecha+"/"+ idBarrio).addValueEventListener(new ValueEventListener() {
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

    private void grabarVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    public void limpiarIV(){
        vvVideoReporte.setVisibility(View.GONE);
        ivImagenReporte.setVisibility(View.VISIBLE);

        ivImagenReporte.setImageResource(R.drawable.ic_image);

        btnEliminarArchivoRep.setEnabled(false);
    }

    public void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//con esto es lo mínimo necesario para abrir la cámara
        startActivityForResult(intent, 1000);//se le pone cualquier número, sirve como código de respeusta
    }

    public void abrirGaleriaFoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 1001);
    }

    public void abrirGaleriaVideo(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, 1002);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000 && resultCode == Activity.RESULT_OK){// el CAMERA_REQUEST es para validar que sea una petición de abrir la cámara y el RESULT_OK es para validar que al abrir la cámara todo salio bien y no hubo errores
            vvVideoReporte.setVisibility(View.GONE);
            ivImagenReporte.setVisibility(View.VISIBLE);

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivImagenReporte.setImageBitmap(imageBitmap);
            btnEliminarArchivoRep.setEnabled(true);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(this.getActivity().getContentResolver(), imageBitmap, "Title", null);
            multimedia = Uri.parse(path);

            btnEliminarArchivoRep.setEnabled(true);
        }

        if(requestCode == 1001 && resultCode == Activity.RESULT_OK){
            vvVideoReporte.setVisibility(View.GONE);
            ivImagenReporte.setVisibility(View.VISIBLE);

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = EnviarReporteFragment.this.getContext().getContentResolver().query(data.getData(), filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap imageBitmap = BitmapFactory.decodeFile(picturePath);
            ivImagenReporte.setImageBitmap(imageBitmap);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(this.getActivity().getContentResolver(), imageBitmap, "Title", null);
            multimedia = Uri.parse(path);

            btnEliminarArchivoRep.setEnabled(true);
        }

        if(requestCode == 1002 && resultCode == Activity.RESULT_OK){
            vvVideoReporte.setVisibility(View.VISIBLE);
            ivImagenReporte.setVisibility(View.GONE);
            vvVideoReporte.setMediaController(mc);

            multimedia = data.getData();
            vvVideoReporte.setVideoURI(multimedia);

            btnEliminarArchivoRep.setEnabled(true);
        }

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            vvVideoReporte.setVisibility(View.VISIBLE);
            ivImagenReporte.setVisibility(View.GONE);

            multimedia = data.getData();
            vvVideoReporte.setVideoURI(multimedia);

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

    private void cargarDatosFirebase(String reporte, String descripcionReporte, String direccion, String referencia, String date2) {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
        String date = dateFormat.format(Calendar.getInstance().getTime());

        if(multimedia!=null){
            subirDataYMultimedia(date2, reporte, descripcionReporte, direccion, referencia, date);
        }
        else {
            Map<String, Object> datosReporte = new HashMap<>();
            datosReporte.put("tipoReporte", nombreDelito);
            datosReporte.put("fecha", date);
            datosReporte.put("ip", getIP());
            datosReporte.put("reporte", reporte);
            datosReporte.put("descripcionReporte", descripcionReporte);
            datosReporte.put("nombreBarrio", nombreBarrio);
            datosReporte.put("direccion", direccion);
            datosReporte.put("referencia", referencia);
            datosReporte.put("linkMultimedia", linkMultimedia);
            datosReporte.put("estado", "ENVIADO");

            mRootReference.child("Reportes/"+mAuth.getUid()+"/"+date2).setValue(datosReporte);
            Toast.makeText(EnviarReporteFragment.this.getContext(), "Reporte enviado", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();

        }
    }


    public void subirDataYMultimedia(String fecha, String reporte, String descripcionReporte, String direccion, String referencia, String date){
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
                            datosReporte.put("tipoReporte", nombreDelito);
                            datosReporte.put("fecha", date);
                            datosReporte.put("ip", getIP());
                            datosReporte.put("reporte", reporte);
                            datosReporte.put("descripcionReporte", descripcionReporte);
                            datosReporte.put("nombreBarrio", nombreBarrio);
                            datosReporte.put("direccion", direccion);
                            datosReporte.put("referencia", referencia);
                            datosReporte.put("linkMultimedia", linkMultimedia);
                            datosReporte.put("estado", "ENVIADO");

                            mRootReference.child("Reportes/"+mAuth.getUid()+"/"+fecha).setValue(datosReporte);
                            Toast.makeText(EnviarReporteFragment.this.getContext(), "Multimedia subido", Toast.LENGTH_SHORT).show();
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
}