package com.example.caxsecurityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrarseActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    AwesomeValidation awesomeValidation;
    private TextInputEditText tieCorreoRegistro, tieContrasenaRegistro, tieContrasenaVerificarRegistro,
            tieNombreRegistro, tieNumeroTelefonoRegistro, tieDNIRegistro;

    DatabaseReference mRootReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        mAuth = FirebaseAuth.getInstance();
        mRootReference = FirebaseDatabase.getInstance().getReference();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.tieNumeroTelefonoRegistro, ".{9,}", R.string.invalid_phone);
        awesomeValidation.addValidation(this, R.id.tieDNIRegistro, ".{8,}", R.string.invalid_dni);
        awesomeValidation.addValidation(this, R.id.tieCorreoRegistro, Patterns.EMAIL_ADDRESS, R.string.invalid_mail);
        awesomeValidation.addValidation(this, R.id.tieContrasenaRegistro, ".{6,}", R.string.invalid_password);
        awesomeValidation.addValidation(this, R.id.tieContrasenaVerificarRegistro, ".{6,}", R.string.invalid_password);

        tieNombreRegistro = findViewById(R.id.tieNombreRegistro);
        tieNumeroTelefonoRegistro = findViewById(R.id.tieNumeroTelefonoRegistro);
        tieDNIRegistro = findViewById(R.id.tieDNIRegistro);
        tieCorreoRegistro = findViewById(R.id.tieCorreoRegistro);
        tieContrasenaRegistro = findViewById(R.id.tieContrasenaRegistro);
        tieContrasenaVerificarRegistro = findViewById(R.id.tieContrasenaVerificarRegistro);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void registrarUsuario(View view){
        String nombre = tieNombreRegistro.getText().toString();
        String telefono = tieNumeroTelefonoRegistro.getText().toString();
        String DNI = tieDNIRegistro.getText().toString();
        String correo = tieCorreoRegistro.getText().toString();
        String contrasena = tieContrasenaRegistro.getText().toString();
        String contrasenaValidar = tieContrasenaVerificarRegistro.getText().toString();

        if(awesomeValidation.validate()){
            if(contrasena.trim().equals(contrasenaValidar.trim())){
                mAuth.createUserWithEmailAndPassword(correo, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(), IniciarSesionActivity.class);
                            Toast.makeText(getApplicationContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                            finish();
                            cargarDatosFirebase(nombre, telefono, DNI, correo);
                            startActivity(intent);
                        }
                        else{
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            dameToastdeerror(errorCode);
                        }
                    }
                });
            }
            else {
                Toast.makeText(this, "Las Contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Completa los campos de manera correcta", Toast.LENGTH_SHORT).show();
        }

    }

    private void cargarDatosFirebase(String nombre, String telefono, String DNI, String correo) {
        Map<String, Object> datosUsuario = new HashMap<>();
        datosUsuario.put("nombre", nombre);
        datosUsuario.put("telefono", telefono);
        datosUsuario.put("dni", DNI);
        datosUsuario.put("correo", correo);

        mRootReference.child("Usuario").push().setValue(datosUsuario);
    }

    private void dameToastdeerror(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(RegistrarseActivity.this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(RegistrarseActivity.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(RegistrarseActivity.this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(RegistrarseActivity.this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                tieCorreoRegistro.setError("La dirección de correo electrónico está mal formateada.");
                tieCorreoRegistro.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(RegistrarseActivity.this, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                tieContrasenaRegistro.setError("la contraseña es incorrecta ");
                tieContrasenaRegistro.requestFocus();
                tieContrasenaRegistro.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(RegistrarseActivity.this, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(RegistrarseActivity.this,"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(RegistrarseActivity.this, "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(RegistrarseActivity.this, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                tieCorreoRegistro.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                tieCorreoRegistro.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(RegistrarseActivity.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(RegistrarseActivity.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(RegistrarseActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(RegistrarseActivity.this, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(RegistrarseActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(RegistrarseActivity.this, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(RegistrarseActivity.this, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                tieContrasenaRegistro.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                tieContrasenaRegistro.requestFocus();
                break;

        }

    }
}