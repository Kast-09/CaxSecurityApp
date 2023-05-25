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

public class SolicitudesAmistadEnviadasAdapter extends RecyclerView.Adapter {
    List<SolicitudAmistad> data;

    public SolicitudesAmistadEnviadasAdapter(List<SolicitudAmistad> data){
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());//aquí llamamos al contexto

        View itemView = inflater.inflate(R.layout.item_solicitudes_enviadas, parent, false);//aquí hacemos referencia al item creado

        return new SolicitudesAmistadEnviadasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView tvCorreoSolicitudAmistad = holder.itemView.findViewById(R.id.tvCorreoSolicitudAmistad);
        tvCorreoSolicitudAmistad.setText(data.get(position).correoUsuarioReceptor);

        TextView tvEstadoSolicitud = holder.itemView.findViewById(R.id.tvEstadoSolicitud);
        tvEstadoSolicitud.setText("PENDIENTE");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class SolicitudesAmistadEnviadasViewHolder extends RecyclerView.ViewHolder {
        public SolicitudesAmistadEnviadasViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
