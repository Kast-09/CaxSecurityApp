package com.example.caxsecurityapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class EnviarReporteFragment extends Fragment {

    private Spinner spTipoReporte, spNombreBarrio;

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enviar_reporte, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spTipoReporte = view.findViewById(R.id.spTipoReporteFragment);
        spNombreBarrio = view.findViewById(R.id.spNombreBarrioFragment);

        ArrayAdapter<CharSequence> adapterspTipoReporte = ArrayAdapter.createFromResource(this.getContext(),
                R.array.combo_tipos, android.R.layout.simple_spinner_dropdown_item);
        spTipoReporte.setAdapter(adapterspTipoReporte);

        ArrayAdapter<CharSequence> adapterspNombreBarrio = ArrayAdapter.createFromResource(this.getContext(),
                R.array.combo_barrios, android.R.layout.simple_spinner_dropdown_item);
        spNombreBarrio.setAdapter(adapterspNombreBarrio);
    }
}