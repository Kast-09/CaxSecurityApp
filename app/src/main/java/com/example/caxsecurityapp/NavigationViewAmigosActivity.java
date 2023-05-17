package com.example.caxsecurityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NavigationViewAmigosActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    VerAmigosFragment verAmigosFragment = new VerAmigosFragment();
    VerSolicitudesAmistadFragment solicitudesAmistadFragment = new VerSolicitudesAmistadFragment();
    EnviarSolicitudAmistadFragment enviarSolicitudAmistadFragment = new EnviarSolicitudAmistadFragment();
    SolicitudesAmistadPendientesFragment solicitudesAmistadPendientesFragment = new SolicitudesAmistadPendientesFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_view_amigos);

        bottomNavigationView = findViewById(R.id.bottom_navigationAmigos);

        getSupportFragmentManager().beginTransaction().replace(R.id.containerAmigos, verAmigosFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ver_amigos:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerAmigos, verAmigosFragment).commit();
                        return true;
                    case R.id.ver_solicitudes_amistad:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerAmigos, solicitudesAmistadFragment).commit();
                        return true;
                    case R.id.ver_enviar_solicitud:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerAmigos, enviarSolicitudAmistadFragment).commit();
                        return true;
                    case R.id.ver_solicitudes_pendientes:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerAmigos, solicitudesAmistadPendientesFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}