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

public class ListadoAmigosAdapter extends RecyclerView.Adapter {

    List<Amigo> data;

    public ListadoAmigosAdapter(List<Amigo> data){
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());//aquí llamamos al contexto

        View itemView = inflater.inflate(R.layout.item_listado_amigos, parent, false);//aquí hacemos referencia al item creado

        return new ListadoAmigosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView tvNombreAmigoContacto = holder.itemView.findViewById(R.id.tvNombreAmigoContacto);
        tvNombreAmigoContacto.setText(data.get(position).nombreAmigo);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ListadoAmigosViewHolder extends RecyclerView.ViewHolder {
        public ListadoAmigosViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
