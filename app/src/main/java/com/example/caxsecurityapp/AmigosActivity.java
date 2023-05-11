package com.example.caxsecurityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AmigosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
    }

    public void verMisAmigos(View view){
        Intent intent = new Intent(getApplicationContext(), VerAmigosActivity.class);
        startActivity(intent);
    }

    public void verSolicitudesAmistad(View view){
        Intent intent = new Intent(getApplicationContext(), SolicitudesAmistadActivity.class);
        startActivity(intent);
    }

    public void enviarSolicitudAmistad(View view){
        Intent intent = new Intent(getApplicationContext(), EnviarSolicitudAmistadActivity.class);
        startActivity(intent);
    }
}