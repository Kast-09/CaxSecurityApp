package com.example.caxsecurityapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.caxsecurityapp.adapters.HistorialReportesAdapter;
import com.example.caxsecurityapp.adapters.SolicitudesAmistadEnviadasAdapter;
import com.example.caxsecurityapp.entities.Reportes;
import com.example.caxsecurityapp.entities.SolicitudAmistad;
import com.example.caxsecurityapp.entities.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SolicitudesAmistadPendientesFragment extends Fragment {


    public String email = "", idUsuario = "";
    DatabaseReference mRootReference;
    List<SolicitudAmistad> dataHistorial = new ArrayList<>();
    RecyclerView rvSolicitudesPendientesFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_solicitudes_amistad_pendientes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRootReference = FirebaseDatabase.getInstance().getReference();
        rvSolicitudesPendientesFragment = view.findViewById(R.id.rvSolicitudesPendientesFragment);

        obtenerCorreoUsuario();
        obtenerDatosUsuario();
        obtenerSolicitudesAmistadEnviadas();
    }

    public void obtenerSolicitudesAmistadEnviadas(){
        mRootReference.child("SolicitudAmistad").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    SolicitudAmistad solicitudAmistad = snapshot.getValue(SolicitudAmistad.class);
                    Log.i("Solicitud", snapshot.toString());
                    if(solicitudAmistad.idUsuarioRemitente.equals(idUsuario)){
                        dataHistorial.add(solicitudAmistad);
                    }
                }

                rvSolicitudesPendientesFragment.setLayoutManager(new LinearLayoutManager(getContext()));
                rvSolicitudesPendientesFragment.setAdapter(new SolicitudesAmistadEnviadasAdapter(dataHistorial));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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