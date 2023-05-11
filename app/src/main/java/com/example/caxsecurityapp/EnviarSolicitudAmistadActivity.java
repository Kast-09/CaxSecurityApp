package com.example.caxsecurityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EnviarSolicitudAmistadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_solicitud_amistad);
    }

    public void enviarSolicitudAmistad(View view){
        Intent intent = new Intent(getApplicationContext(), SolicitudesAmistadEnviadasActivity.class);
        startActivity(intent);
    }
}