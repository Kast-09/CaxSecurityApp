package com.example.caxsecurityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CodigoRecuperacionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_recuperacion);
    }

    public void irRestablecerContrasena(View view){
        Intent intent = new Intent(this, RestablecerContrasenaActivity.class);
        startActivity(intent);
    }
}