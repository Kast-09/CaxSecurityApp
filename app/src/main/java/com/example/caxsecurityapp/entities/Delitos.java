package com.example.caxsecurityapp.entities;

public class Delitos {
    public String id;
    public String nombreDelito;

    public Delitos() {
    }

    public Delitos(String id, String nombreDelito) {
        this.id = id;
        this.nombreDelito = nombreDelito;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreDelito() {
        return nombreDelito;
    }

    public void setNombreDelito(String nombreDelito) {
        this.nombreDelito = nombreDelito;
    }

    @Override
    public String toString() {
        return nombreDelito;
    }
}
