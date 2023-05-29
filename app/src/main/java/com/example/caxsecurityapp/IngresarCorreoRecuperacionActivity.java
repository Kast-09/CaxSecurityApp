package com.example.caxsecurityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IngresarCorreoRecuperacionActivity extends AppCompatActivity {

    private TextInputEditText tilCorreoRecuperacionET;
    AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_correo_recuperacion);

        tilCorreoRecuperacionET = findViewById(R.id.tilCorreoRecuperacionET);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.tilCorreoRecuperacionET, Patterns.EMAIL_ADDRESS, R.string.invalid_mail);
    }

    public void enviarCodigoRecuperacion(View view){
        if (awesomeValidation.validate()){
            String correo = tilCorreoRecuperacionET.getText().toString();
            enviarCorreo(correo);
        }
    }

    public void enviarCorreo(String correo){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = correo;
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Correo de recuperación enviado", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(IngresarCorreoRecuperacionActivity.this, IniciarSesionActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Correo invalido", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        //esta funcion sirve para configurar a donde se va a ridirigir cuando se haga click atras
        super.onBackPressed();

        Intent intent = new Intent(IngresarCorreoRecuperacionActivity.this, IniciarSesionActivity.class);
        startActivity(intent);
        finish();
    }
}