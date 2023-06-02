package com.example.caxsecurityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ReportesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);
    }

    public void enviarReporte(View view){
        /*Intent intent = new Intent(getApplicationContext(), EnviarReporteActivity.class);
        startActivity(intent);*/
    }

    public void verHistorialReportes(View view){
        Intent intent = new Intent(getApplicationContext(), HistorialReportesActivity.class);
        startActivity(intent);
    }
}