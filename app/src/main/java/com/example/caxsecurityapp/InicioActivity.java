package com.example.caxsecurityapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class InicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
    }

    public void verMiPerfil(View view){
        Intent intent = new Intent(getApplicationContext(), MiPerfilActivity.class);
        startActivity(intent);
    }

    public void irReportes(View view){
        Intent intent = new Intent(getApplicationContext(), NavigationViewReportesActivity.class);
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

    //presionar boton atras
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea salir de la aplicación?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}