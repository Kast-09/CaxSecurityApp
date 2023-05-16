package com.example.caxsecurityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class InicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
    }

    public void cerrarSesion(View view){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(), "Sesión Cerrada", Toast.LENGTH_SHORT).show();
        irLogin();
    }

    public void irLogin(){
        Intent intent = new Intent(getApplicationContext(), IniciarSesionActivity.class);
        startActivity(intent);
    }
}