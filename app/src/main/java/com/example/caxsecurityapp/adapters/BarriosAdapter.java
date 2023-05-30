package com.example.caxsecurityapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caxsecurityapp.R;
import com.example.caxsecurityapp.entities.Barrios;

import java.util.List;

public class BarriosAdapter extends RecyclerView.Adapter {

    List<Barrios> data;

    public BarriosAdapter(List<Barrios> data){
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
        /*TextView tvNombreBarrio = holder.itemView.findViewById(R.id.tvNombreBarrio);
        tvNombreBarrio.setText(data.get(position).nombreBarrio);

        TextView tvDescripcionBarrio = holder.itemView.findViewById(R.id.tvDescripcionBarrio);
        tvDescripcionBarrio.setText(data.get(position).estadoBarrio);*/
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
