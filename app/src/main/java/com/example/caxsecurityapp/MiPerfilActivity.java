package com.example.caxsecurityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.caxsecurityapp.entities.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MiPerfilActivity extends AppCompatActivity {

    DatabaseReference mRootReference;
    private String email;
    private TextView tvNombreUsuarioPerfil, tvDNIUsuarioPerfil, tvTelefonoUsuarioPerfil, tvCorreoUsuarioPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        mRootReference = FirebaseDatabase.getInstance().getReference();

        tvNombreUsuarioPerfil = findViewById(R.id.tvNombreUsuarioPerfil);
        tvDNIUsuarioPerfil = findViewById(R.id.tvDNIUsuarioPerfil);
        tvTelefonoUsuarioPerfil = findViewById(R.id.tvTelefonoUsuarioPerfil);
        tvCorreoUsuarioPerfil = findViewById(R.id.tvCorreoUsuarioPerfil);


        obtenerCorreoUsuario();
        obtenerDatosUsuario();

    }

    public void irActualizarContrasena(View view){
        Intent intent = new Intent(getApplicationContext(), ActualizarContrasenaActivity.class);
        startActivity(intent);
    }

    public void obtenerCorreoUsuario(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            Log.i("ID", email);
        }
    }

    public void obtenerDatosUsuario(){
        mRootReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    mRootReference.child("Usuario").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Usuario user = snapshot.getValue(Usuario.class);
                            if(user.correo.equalsIgnoreCase(email)){
                                tvNombreUsuarioPerfil.setText(user.nombre);
                                tvDNIUsuarioPerfil.setText(user.dni);
                                tvTelefonoUsuarioPerfil.setText(user.telefono);
                                tvCorreoUsuarioPerfil.setText(user.correo);
                            }
                            Log.i("ID", snapshot.getKey());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(getApplicationContext(), InicioActivity.class);
        startActivity(intent);
        return super.onKeyDown(keyCode, event);
    }
}