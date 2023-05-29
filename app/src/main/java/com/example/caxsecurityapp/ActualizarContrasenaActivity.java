package com.example.caxsecurityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActualizarContrasenaActivity extends AppCompatActivity {

    TextInputEditText tieNuevaContrasenaActualizar, tieVerificarContrasenaActualizar;
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_contrasena);

        tieNuevaContrasenaActualizar = findViewById(R.id.tieNuevaContrasenaActualizar);
        tieVerificarContrasenaActualizar = findViewById(R.id.tieVerificarContrasenaActualizar);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.tieNuevaContrasenaActualizar, ".{6,}", R.string.invalid_password);
        awesomeValidation.addValidation(this, R.id.tieVerificarContrasenaActualizar, ".{6,}", R.string.invalid_password);
    }

    public void actualizarContrasena(View view){
        String contrasena = "";
        if(awesomeValidation.validate()){
            if(tieNuevaContrasenaActualizar.getText().toString().trim().equals(tieVerificarContrasenaActualizar.getText().toString().trim())){
                contrasena = tieNuevaContrasenaActualizar.getText().toString();
                Intent intent = new Intent(getApplicationContext(), MiPerfilActivity.class);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user.updatePassword(contrasena).isSuccessful()){
                    Toast.makeText(this,"La contraseña se actualizo con exito", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this,"La contraseña no se pudo actualizar, intentelo de nuevo más tarde", Toast.LENGTH_LONG).show();
                }
                startActivity(intent);
            }
            else{
                Toast.makeText(this,"Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this,"Complete los campos correctamente", Toast.LENGTH_LONG).show();
        }
    }

    public void cancelarCambioContrasena(View view){
        Intent intent = new Intent(getApplicationContext(), MiPerfilActivity.class);
        startActivity(intent);
    }
}