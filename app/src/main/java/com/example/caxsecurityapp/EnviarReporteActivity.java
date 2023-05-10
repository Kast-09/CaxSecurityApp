package com.example.caxsecurityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class EnviarReporteActivity extends AppCompatActivity {

    private Spinner spTipoReporte, spNombreBarrio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );*/
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
}