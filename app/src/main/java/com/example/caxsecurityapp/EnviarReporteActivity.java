package com.example.caxsecurityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class EnviarReporteActivity extends AppCompatActivity {

    private Spinner spTipoReporte, spNombreBarrio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_reporte);

        spTipoReporte = findViewById(R.id.spTipoReporte);
        spNombreBarrio = findViewById(R.id.spNombreBarrio);

        ArrayAdapter<CharSequence> adapterspTipoReporte = ArrayAdapter.createFromResource(this,
                R.array.combo_tipos, android.R.layout.simple_spinner_dropdown_item);
        spTipoReporte.setAdapter(adapterspTipoReporte);

        ArrayAdapter<CharSequence> adapterspNombreBarrio = ArrayAdapter.createFromResource(this,
                R.array.combo_barrios, android.R.layout.simple_spinner_dropdown_item);
        spNombreBarrio.setAdapter(adapterspNombreBarrio);
    }

    public void enviarReporte(View view){
        Intent intent = new Intent(getApplicationContext(), HistorialReportesActivity.class);
        startActivity(intent);
    }
}