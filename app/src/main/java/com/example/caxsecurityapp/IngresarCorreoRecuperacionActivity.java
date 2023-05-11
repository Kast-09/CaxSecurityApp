package com.example.caxsecurityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class IngresarCorreoRecuperacionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_correo_recuperacion);
    }

    public void enviarCodigoRecuperacion(View view){

    }

    public void irValidarCodigoRecuperacion(View view){
        Intent intent = new Intent(this, CodigoRecuperacionActivity.class);
        startActivity(intent);
    }
}