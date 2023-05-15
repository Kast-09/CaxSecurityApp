package com.example.caxsecurityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class IniciarSesionActivity extends AppCompatActivity {

    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth mAuth;
    private TextInputLayout tilCorreo, tilContrasena;
    private TextInputEditText tilCorreoET, tilContrasenaET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_CaxSecurityApp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        tilCorreoET = findViewById(R.id.tilCorreoET);
        tilContrasenaET = findViewById(R.id.tilContrasenaET);

        firebaseAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.tilCorreoET, Patterns.EMAIL_ADDRESS, R.string.invalid_mail);
        awesomeValidation.addValidation(this, R.id.tilContrasenaET, ".{6,}", R.string.invalid_password);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user!=null){
            irHome();
        }
    }

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void iniciarSesion(View view){
        if (awesomeValidation.validate()){
            String correo = tilCorreoET.getText().toString();
            String contrasena = tilContrasenaET.getText().toString();

            firebaseAuth.signInWithEmailAndPassword(correo, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        Toast.makeText(getApplicationContext(), "Inicio de Sesión Exitoso", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                    else {
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        dameToastdeerror(errorCode);
                    }
                }
            });
        }
    }

    public void irRegistro(View view){
        Intent intent = new Intent(this, RegistrarseActivity.class);
        startActivity(intent);
    }

    public void irOlvideContrasena(View view){
        Intent intent = new Intent(this, IngresarCorreoRecuperacionActivity.class);
        startActivity(intent);
    }

    public void irHome(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        //intent.putExtra("correo", etCorreo.getText().toString());
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void dameToastdeerror(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(IniciarSesionActivity.this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(IniciarSesionActivity.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(IniciarSesionActivity.this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(IniciarSesionActivity.this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                tilCorreoET.setError("La dirección de correo electrónico está mal formateada.");
                tilCorreoET.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(IniciarSesionActivity.this, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                tilContrasenaET.setError("la contraseña es incorrecta ");
                tilContrasenaET.requestFocus();
                tilContrasenaET.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(IniciarSesionActivity.this, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(IniciarSesionActivity.this,"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(IniciarSesionActivity.this, "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(IniciarSesionActivity.this, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                tilCorreoET.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                tilCorreoET.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(IniciarSesionActivity.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(IniciarSesionActivity.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(IniciarSesionActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(IniciarSesionActivity.this, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(IniciarSesionActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(IniciarSesionActivity.this, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(IniciarSesionActivity.this, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                tilContrasenaET.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                tilContrasenaET.requestFocus();
                break;

        }

    }
}