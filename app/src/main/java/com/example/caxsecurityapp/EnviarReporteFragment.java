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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

public class EnviarReporteFragment extends Fragment {

    private Spinner spTipoReporte, spNombreBarrio;
    private RadioButton rbReporteAnonimo, rbReportePublico;
    private TextInputEditText tieDescripcionReporte, tieDireccionReporte, tieReferenciaReporte;
    DatabaseReference mRootReference;
    public String email = "", idUsuario = "";
    private Button btnSendReport;

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

        obtenerCorreoUsuario();
        obtenerDatosUsuario();

        ArrayAdapter<CharSequence> adapterspTipoReporte = ArrayAdapter.createFromResource(this.getContext(),
                R.array.combo_tipos, android.R.layout.simple_spinner_dropdown_item);
        spTipoReporte.setAdapter(adapterspTipoReporte);

        ArrayAdapter<CharSequence> adapterspNombreBarrio = ArrayAdapter.createFromResource(this.getContext(),
                R.array.combo_barrios, android.R.layout.simple_spinner_dropdown_item);
        spNombreBarrio.setAdapter(adapterspNombreBarrio);

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
                String nombreBarrio = spNombreBarrio.getSelectedItem().toString();
                String direccion = tieDireccionReporte.getText().toString();
                String referencia = tieReferenciaReporte.getText().toString();

                cargarDatosFirebase(tipoReporte, reporte, descripcionReporte, nombreBarrio, direccion, referencia);

                Toast.makeText(view.getContext(), "Reporte enviado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*public void enviarReporte(View view){
        String tipoReporte = "";
        if(rbReporteAnonimo.isSelected()){
            tipoReporte = rbReporteAnonimo.getText().toString();
        }
        else if(rbReportePublico.isSelected()){
            tipoReporte = rbReporteAnonimo.getText().toString();
        }
        String reporte = spTipoReporte.getSelectedItem().toString();
        String descripcionReporte = tieDescripcionReporte.getText().toString();
        String nombreBarrio = spNombreBarrio.getSelectedItem().toString();
        String direccion = tieDireccionReporte.getText().toString();
        String referencia = tieReferenciaReporte.getText().toString();

        cargarDatosFirebase(tipoReporte, reporte, descripcionReporte, nombreBarrio, direccion, referencia);

        Toast.makeText(this.getContext(), "Reporte enviado", Toast.LENGTH_SHORT).show();
    }*/

    private void cargarDatosFirebase(String tipoReporte, String reporte, String descripcionReporte, String nombreBarrio, String direccion, String referencia) {

        Map<String, Object> datosReporte = new HashMap<>();
        datosReporte.put("idUsuario", idUsuario);
        datosReporte.put("tipoReporte", tipoReporte);
        datosReporte.put("reporte", reporte);
        datosReporte.put("descripcionReporte", descripcionReporte);
        datosReporte.put("nombreBarrio", nombreBarrio);
        datosReporte.put("referencia", referencia);
        datosReporte.put("estado", "ENVIADO");

        mRootReference.child("Reportes").push().setValue(datosReporte);
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