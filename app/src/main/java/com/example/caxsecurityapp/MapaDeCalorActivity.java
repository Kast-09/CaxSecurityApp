package com.example.caxsecurityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.caxsecurityapp.adapters.BarriosAdapter;
import com.example.caxsecurityapp.adapters.HistorialReportesAdapter;
import com.example.caxsecurityapp.entities.Barrio_MapaCalor;
import com.example.caxsecurityapp.entities.Barrios;
import com.example.caxsecurityapp.entities.EstadoBarrio;
import com.example.caxsecurityapp.entities.Reportes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MapaDeCalorActivity extends AppCompatActivity {

    DatabaseReference mRootReference;
    List<Barrios> listBarrios = new ArrayList<>();
    RecyclerView rvListadoBarrios;
    ProgressDialog progressDialog;

    String id = "";
    String nombre = "";
    String estado = "";
    int CantReportes = 0;
    int cont = 0;
    int cont2 = 0;
    final List<Barrios> barriosList = new ArrayList<>();
    final List<Barrio_MapaCalor> barriomapaCalors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_de_calor);

        mRootReference = FirebaseDatabase.getInstance().getReference();
        rvListadoBarrios = findViewById(R.id.rvListadoBarrios);
        progressDialog = new ProgressDialog(this);

        obtenerMapaCalor();
    }

    public void obtenerMapaCalor(){
        DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        progressDialog.setMessage("Cargando data");
        progressDialog.show();
        mRootReference.child("Barrios/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(cont>0){
                        if(snapshot.exists()){
                            id = ""+cont;
                            nombre = snapshot.child("nombre").getValue().toString();

                            mRootReference.child("EstadoBarrios/"+date+"/"+cont2).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                    for(DataSnapshot snapshot : snapshot2.getChildren()){
                                        if(snapshot2.exists()){
                                            CantReportes += Integer.parseInt(snapshot2.child("d1").getValue().toString());
                                            CantReportes += Integer.parseInt(snapshot2.child("d2").getValue().toString());
                                            CantReportes += Integer.parseInt(snapshot2.child("d3").getValue().toString());
                                        }
                                    }
                                    rvListadoBarrios.setLayoutManager(new LinearLayoutManager(MapaDeCalorActivity.this));
                                    rvListadoBarrios.setAdapter(new BarriosAdapter(barriomapaCalors));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            if(CantReportes > 0 && CantReportes < 5){
                                estado = "El barrio esta teniendo problemas.\n¡Se han reportado incidencias!";
                            }
                            else if(CantReportes > 5){
                                estado = "El barrio esta teniendo muchos problemas.\n¡Se han reportado demasiadas incidencias!";
                            }
                            else{
                                estado = "El barrio esta tranquilo.\n¡No se han reportado incidencias por ahora!";
                            }
                            barriomapaCalors.add(new Barrio_MapaCalor(id, nombre, estado, CantReportes));
                        }
                    }
                    cont++;
                }
                rvListadoBarrios.setLayoutManager(new LinearLayoutManager(MapaDeCalorActivity.this));
                rvListadoBarrios.setAdapter(new BarriosAdapter(barriomapaCalors));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        progressDialog.dismiss();
    }
}