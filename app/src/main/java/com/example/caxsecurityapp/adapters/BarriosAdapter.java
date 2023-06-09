package com.example.caxsecurityapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caxsecurityapp.R;
import com.example.caxsecurityapp.entities.Barrio_MapaCalor;
import com.example.caxsecurityapp.entities.Barrios;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BarriosAdapter extends RecyclerView.Adapter {

    List<Barrio_MapaCalor> data;

    public BarriosAdapter(List<Barrio_MapaCalor> data){
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.item_barrios, parent, false);

        return new BarriosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView tvNombreBarrio = holder.itemView.findViewById(R.id.tvNombreBarrio);
        tvNombreBarrio.setText(data.get(position).nombre);

        TextView tvDescripcionBarrio = holder.itemView.findViewById(R.id.tvDescripcionBarrio);
        tvDescripcionBarrio.setText(data.get(position).estado);

        ImageView ivEstadoBarrio = holder.itemView.findViewById(R.id.ivEstadoBarrio);
        int temp = data.get(position).CantReportes;
        if(temp == 0){
            ivEstadoBarrio.setImageResource(R.drawable.ic_health);
        }
        else if(temp > 0 && temp < 5){
            ivEstadoBarrio.setImageResource(R.drawable.ic_alerta);
        }
        else if(temp > 5){
            ivEstadoBarrio.setImageResource(R.drawable.ic_peligro);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class BarriosViewHolder extends RecyclerView.ViewHolder {
        public BarriosViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
