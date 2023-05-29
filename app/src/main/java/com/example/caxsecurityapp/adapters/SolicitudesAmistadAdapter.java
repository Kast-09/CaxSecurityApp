package com.example.caxsecurityapp.adapters;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caxsecurityapp.MiPerfilActivity;
import com.example.caxsecurityapp.R;
import com.example.caxsecurityapp.entities.SolicitudAmistad;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SolicitudesAmistadAdapter extends RecyclerView.Adapter {

    List<SolicitudAmistad> data;

    public SolicitudesAmistadAdapter(List<SolicitudAmistad> data){
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());//aquí llamamos al contexto

        View itemView = inflater.inflate(R.layout.item_solicitudes_amistad, parent, false);//aquí hacemos referencia al item creado

        return new SolicitudesAmistadViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView tvNombreSolicitudAmistad = holder.itemView.findViewById(R.id.tvNombreSolicitudAmistad);
        tvNombreSolicitudAmistad.setText(data.get(position).nombreUsuarioRemitente);

        ImageView ivPerfilSolicutud = holder.itemView.findViewById(R.id.ivPerfilSolicutud);
        Log.i("linkFoto", data.get(position).fotoUsuario.toString());
        if(!data.get(position).fotoUsuario.toString().equals("")){
            Picasso.with(holder.itemView.getContext())
                    .load(data.get(position).fotoUsuario)
                    .resize(120, 120)
                    .into(ivPerfilSolicutud);
        }
        else{
            Picasso.with(holder.itemView.getContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/caxsecurity.appspot.com/o/fotosPerfil%2Fic_user_default.png?alt=media&token=89b7e229-0e17-467a-b94c-287e28347310")
                    .resize(120, 120)
                    .into(ivPerfilSolicutud);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class SolicitudesAmistadViewHolder extends RecyclerView.ViewHolder {
        public SolicitudesAmistadViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
