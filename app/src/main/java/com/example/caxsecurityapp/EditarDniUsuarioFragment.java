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
    TextInputEditText tieDNIEditarUsuario;
    DatabaseReference mRootReference;
    private FirebaseAuth mAuth;
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
        mAuth = FirebaseAuth.getInstance();

        tieDNIEditarUsuario = view.findViewById(R.id.tieDNIEditarUsuario);

        btnEditDNI = view.findViewById(R.id.btnEditTelefono);

        obtenerDatosUsuario();

        btnEditDNI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tieDNIEditarUsuario.getText().toString().length() == 8){
                    Map<String, Object> actualizarNombre = new HashMap<>();
                    actualizarNombre.put("dni", tieDNIEditarUsuario.getText().toString());
                    mRootReference.child("Usuario/"+mAuth.getUid()).updateChildren(actualizarNombre).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                else{
                    Toast.makeText(getContext(), "El número de DNI debe tener 8 dígitos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void obtenerDatosUsuario(){
        mRootReference.child("Usuario/"+mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario user = dataSnapshot.getValue(Usuario.class);
                tieDNIEditarUsuario.setText(user.dni);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}