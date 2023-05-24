package com.example.caxsecurityapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caxsecurityapp.R;
import com.example.caxsecurityapp.entities.SolicitudAmistad;

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
