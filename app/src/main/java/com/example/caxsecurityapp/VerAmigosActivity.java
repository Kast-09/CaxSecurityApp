package com.example.caxsecurityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class VerAmigosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_amigos);
    }

    public void verSolicitudesEnviadas(View view){
        Intent intent = new Intent(getApplicationContext(), SolicitudesAmistadEnviadasActivity.class);
        startActivity(intent);
    }
}