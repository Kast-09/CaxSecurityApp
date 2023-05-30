package com.example.caxsecurityapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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

public class EditarTelefonoUsuarioFragment extends DialogFragment {

    TextInputEditText tieTelefonoEditarUsuario;
    DatabaseReference mRootReference;
    Button btnEditTelefono;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_telefono_usuario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRootReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        tieTelefonoEditarUsuario = view.findViewById(R.id.tieTelefonoEditarUsuario);
        btnEditTelefono = view.findViewById(R.id.btnEditTelefono);

        obtenerDatosUsuario();

        btnEditTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> actualizarNombre = new HashMap<>();
                actualizarNombre.put("telefono", tieTelefonoEditarUsuario.getText().toString());
                Log.i("ID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                mRootReference.child("Usuario/"+mAuth.getUid()).updateChildren(actualizarNombre).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Número de teléfono de usuario actualizado", Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "No se pudo actualizar el número de teléfono", Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                });
            }
        });
    }

    public void obtenerDatosUsuario(){
        mRootReference.child("Usuario/"+mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario user = dataSnapshot.getValue(Usuario.class);
                tieTelefonoEditarUsuario.setText(user.telefono);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}