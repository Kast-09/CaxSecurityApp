package com.example.caxsecurityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NavigationViewReportesActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    EnviarReporteFragment enviarReporteFragment = new EnviarReporteFragment();
    HistorialReportesFragment historialReportesFragment = new HistorialReportesFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_view_reportes);

        bottomNavigationView = findViewById(R.id.bottom_navigationReportes);

        getSupportFragmentManager().beginTransaction().replace(R.id.containerReportes, enviarReporteFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.enviar_reporte:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerReportes, enviarReporteFragment).commit();
                        return true;
                    case R.id.ver_historial_reportes:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerReportes, historialReportesFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}