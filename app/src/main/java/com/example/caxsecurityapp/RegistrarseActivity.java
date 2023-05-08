package com.example.caxsecurityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrarseActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText etCorreoRegistro, etPasswordRegistro, etPasswordRegistro2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        mAuth = FirebaseAuth.getInstance();

        etCorreoRegistro = findViewById(R.id.etCorreoRegistro);
        etPasswordRegistro = findViewById(R.id.etPasswordRegistro);
        etPasswordRegistro2 = findViewById(R.id.etPasswordRegistro2);
    }

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void registrarUsuario(View view){

        if(etPasswordRegistro.getText().toString().trim().equals(etPasswordRegistro2.getText().toString().trim())){
            mAuth.createUserWithEmailAndPassword(etCorreoRegistro.getText().toString().trim(), etPasswordRegistro.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                Toast.makeText(getApplicationContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                //updateUI(user);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Error al Registrar", Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        }
        else {
            Toast.makeText(this, "Las Contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }

    }
}