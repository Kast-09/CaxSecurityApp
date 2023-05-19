package com.example.caxsecurityapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caxsecurityapp.R;
import com.example.caxsecurityapp.entities.Amigo;

import java.util.List;

public class AmigosAdapter extends RecyclerView.Adapter {

    List<Amigo> data;

    public AmigosAdapter(List<Amigo> data){
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());//aquí llamamos al contexto

        View itemView = inflater.inflate(R.layout.item_amigos, parent, false);//aquí hacemos referencia al item creado

        return new AmigosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView tvNombreAmigo = holder.itemView.findViewById(R.id.tvNombreAmigo);
        tvNombreAmigo.setText(data.get(position).nombreAmigo);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class AmigosViewHolder extends RecyclerView.ViewHolder {
        public AmigosViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
