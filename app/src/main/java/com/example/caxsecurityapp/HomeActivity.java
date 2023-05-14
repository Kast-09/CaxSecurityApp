package com.example.caxsecurityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void verMiPerfil(View view){
        Intent intent = new Intent(getApplicationContext(), MiPerfilActivity.class);
        startActivity(intent);
    }

    public void verViewAmigos(View view){
        Intent intent = new Intent(getApplicationContext(), AmigosActivity.class);
        startActivity(intent);
    }

    public void irReportes(View view){
        Intent intent = new Intent(getApplicationContext(), ReportesActivity.class);
        startActivity(intent);
    }

    public void irViewCompartirMiUbicacion(View view){
        Intent intent = new Intent(getApplicationContext(), CompartirUbicacionActivity.class);
        startActivity(intent);
    }

    public void verMapaCalor(View view){
        Intent intent = new Intent(getApplicationContext(), MapaDeCalorActivity.class);
        startActivity(intent);
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