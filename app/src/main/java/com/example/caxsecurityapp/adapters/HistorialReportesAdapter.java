package com.example.caxsecurityapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caxsecurityapp.R;
import com.example.caxsecurityapp.entities.Reportes;

import java.util.ArrayList;
import java.util.List;

public class HistorialReportesAdapter extends RecyclerView.Adapter {

    List<Reportes> data;

    public HistorialReportesAdapter(List<Reportes> data){
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.item_historial_reportes, parent, false);

        return new HistorialReportesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView tvHistorialTipoReporte = holder.itemView.findViewById(R.id.tvHistorialTipoReporte);
        tvHistorialTipoReporte.setText(data.get(position).reporte);

        TextView tvHistorialDescripcionReporte = holder.itemView.findViewById(R.id.tvHistorialDescripcionReporte);
        tvHistorialDescripcionReporte.setText(data.get(position).descripcionReporte);

        TextView tvEstadoReporte = holder.itemView.findViewById(R.id.tvEstadoReporte);
        tvEstadoReporte.setText(data.get(position).estado);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class HistorialReportesViewHolder extends RecyclerView.ViewHolder{
        public HistorialReportesViewHolder(@NonNull View itemView) {super(itemView);}
    }
}
