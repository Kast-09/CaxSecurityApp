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
import com.example.caxsecurityapp.entities.Reportes;
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


public class HistorialReportesFragment extends Fragment {
    DatabaseReference mRootReference;
    List<Reportes> dataReportes = new ArrayList<>();
    RecyclerView rvHistorialReportes;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_historial_reportes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRootReference = FirebaseDatabase.getInstance().getReference();
        rvHistorialReportes = view.findViewById(R.id.rvHistorialReportes);

        dataReportes.clear();

        mAuth = FirebaseAuth.getInstance();

        obtenerReportesUsuario();
    }

    public void obtenerReportesUsuario(){
        mRootReference.child("Reportes/"+mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Reportes reportes = snapshot.getValue(Reportes.class);
                    dataReportes.add(reportes);
                }

                rvHistorialReportes.setLayoutManager(new LinearLayoutManager(getContext()));
                rvHistorialReportes.setAdapter(new HistorialReportesAdapter(dataReportes));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}