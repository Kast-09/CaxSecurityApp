package com.example.caxsecurityapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.caxsecurityapp.entities.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditarDniUsuarioFragment extends DialogFragment {
    String email, idUser;
    TextInputEditText tieDNIEditarUsuario;
    DatabaseReference mRootReference;

    Button btnEditDNI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editar_dni_usuario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRootReference = FirebaseDatabase.getInstance().getReference();

        tieDNIEditarUsuario = view.findViewById(R.id.tieDNIEditarUsuario);

        btnEditDNI = view.findViewById(R.id.btnEditTelefono);

        obtenerCorreoUsuario();
        obtenerDatosUsuario();

        btnEditDNI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> actualizarNombre = new HashMap<>();
                actualizarNombre.put("dni", tieDNIEditarUsuario.getText().toString());
                Log.i("ID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                mRootReference.child("Usuario").child(idUser).updateChildren(actualizarNombre).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "DNI de usuario actualizado", Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "No se pudo actualizar el DNI", Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                });
            }
        });
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
                                tieDNIEditarUsuario.setText(user.dni);
                                idUser = snapshot.getKey();
                            }
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
}