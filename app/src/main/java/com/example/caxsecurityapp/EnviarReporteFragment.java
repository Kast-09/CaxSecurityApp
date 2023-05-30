package com.example.caxsecurityapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.caxsecurityapp.entities.Barrios;
import com.example.caxsecurityapp.entities.Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.collections.ArrayDeque;

public class EnviarReporteFragment extends Fragment {

    private Spinner spTipoReporte, spNombreBarrio;
    private RadioButton rbReporteAnonimo, rbReportePublico;
    private TextInputEditText tieDescripcionReporte, tieDireccionReporte, tieReferenciaReporte;
    DatabaseReference mRootReference;
    public String email = "", idUsuario = "", idBarrio = "";
    private Button btnSendReport, btnTomarFotoRep, btnGrabarVideoRep, btnSeleccionarArchivoRep, btnEliminarArchivoRep;

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
        btnSendReport = view.findViewById(R.id.btnSendReport);
        btnTomarFotoRep = view.findViewById(R.id.btnTomarFotoRep);
        btnGrabarVideoRep = view.findViewById(R.id.btnGrabarVideoRep);
        btnSeleccionarArchivoRep = view.findViewById(R.id.btnSeleccionarArchivoRep);
        btnEliminarArchivoRep = view.findViewById(R.id.btnEliminarArchivoRep);

        obtenerCorreoUsuario();
        obtenerDatosUsuario();

        ArrayAdapter<CharSequence> adapterspTipoReporte = ArrayAdapter.createFromResource(this.getContext(),
                R.array.combo_tipos, android.R.layout.simple_spinner_dropdown_item);
        spTipoReporte.setAdapter(adapterspTipoReporte);

        obtenerBarrios();
        /*ArrayAdapter<CharSequence> adapterspNombreBarrio = ArrayAdapter.createFromResource(this.getContext(),
                R.array.combo_barrios, android.R.layout.simple_spinner_dropdown_item);
        spNombreBarrio.setAdapter(adapterspNombreBarrio);*/

        btnTomarFotoRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnGrabarVideoRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSeleccionarArchivoRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnEliminarArchivoRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tipoReporte = "";
                if(rbReporteAnonimo.isChecked()){
                    tipoReporte = rbReporteAnonimo.getText().toString();
                }
                else if(rbReportePublico.isChecked()){
                    tipoReporte = rbReporteAnonimo.getText().toString();
                }
                String reporte = spTipoReporte.getSelectedItem().toString();
                String descripcionReporte = tieDescripcionReporte.getText().toString();
                String direccion = tieDireccionReporte.getText().toString();
                String referencia = tieReferenciaReporte.getText().toString();

                cargarDatosFirebase(tipoReporte, reporte, descripcionReporte, direccion, referencia);

                Toast.makeText(view.getContext(), "Reporte enviado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void obtenerBarrios(){
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
                            idBarrio = adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void cargarDatosFirebase(String tipoReporte, String reporte, String descripcionReporte, String direccion, String referencia) {

        Map<String, Object> datosReporte = new HashMap<>();
        datosReporte.put("idUsuario", idUsuario);
        datosReporte.put("tipoReporte", tipoReporte);
        datosReporte.put("reporte", reporte);
        datosReporte.put("descripcionReporte", descripcionReporte);
        datosReporte.put("nombreBarrio", idBarrio);
        datosReporte.put("referencia", referencia);
        datosReporte.put("estado", "ENVIADO");

        mRootReference.child("Reportes/Usuari1").push().setValue(datosReporte);
    }

    public void obtenerCorreoUsuario(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }
    }

    public void obtenerDatosUsuario(){
        mRootReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    mRootReference.child("Usuario").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Usuario user = snapshot.getValue(Usuario.class);
                            if(user.correo.equals(email)){
                                idUsuario = snapshot.getKey().toString();
                            }
                            Log.i("ID", snapshot.getKey().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}