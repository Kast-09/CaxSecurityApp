package com.example.caxsecurityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActualizarContrasenaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_contrasena);
    }

    public void actualizarContrasena(View view){
        Intent intent = new Intent(getApplicationContext(), MiPerfilActivity.class);
        startActivity(intent);
    }

    public void cancelarCambioContrasena(View view){
        Intent intent = new Intent(getApplicationContext(), MiPerfilActivity.class);
        startActivity(intent);
    }
}