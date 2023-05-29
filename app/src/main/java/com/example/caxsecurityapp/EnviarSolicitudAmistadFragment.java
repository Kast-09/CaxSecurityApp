package com.example.caxsecurityapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.caxsecurityapp.entities.Usuario;
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

public class EnviarSolicitudAmistadFragment extends Fragment {

    public String email = "", idUsuario = "", nombreUsuario = "", fotoUsuario = "";
    TextInputEditText tieCorreoSolicitudAmistad;
    DatabaseReference mRootReference;
    Button btnEnviarSolicitudAmistad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enviar_solicitud_amistad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRootReference = FirebaseDatabase.getInstance().getReference();
        tieCorreoSolicitudAmistad = view.findViewById(R.id.tieCorreoSolicitudAmistad);
        btnEnviarSolicitudAmistad = view.findViewById(R.id.btnEnviarSolicitudAmistad);

        obtenerCorreoUsuario();
        obtenerDatosUsuario();

        btnEnviarSolicitudAmistad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correoAmigo = tieCorreoSolicitudAmistad.getText().toString();
                cargarDatosFirebase(correoAmigo);
                Toast.makeText(view.getContext(), "Solicitud Enviada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarDatosFirebase(String correoAmigo) {

        Map<String, Object> datosSolicitud = new HashMap<>();
        datosSolicitud.put("idUsuarioRemitente", idUsuario);
        datosSolicitud.put("fotoUsuario", fotoUsuario);
        datosSolicitud.put("nombreUsuarioRemitente", nombreUsuario);
        datosSolicitud.put("correoUsuarioReceptor", correoAmigo);

        mRootReference.child("SolicitudAmistad").push().setValue(datosSolicitud);
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
                                idUsuario = snapshot.getKey();
                                nombreUsuario = user.nombre;
                                fotoUsuario = user.photo;
                            }
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