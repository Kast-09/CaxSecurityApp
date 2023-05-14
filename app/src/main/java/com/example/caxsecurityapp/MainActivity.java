package com.example.caxsecurityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void viewIniciarSesion(View view){
        Intent intent = new Intent(this, IniciarSesionActivity.class);
        startActivity(intent);
    }

    public void viewRegistro(View view){
        Intent intent = new Intent(this, RegistrarseActivity.class);
        startActivity(intent);
    }
}