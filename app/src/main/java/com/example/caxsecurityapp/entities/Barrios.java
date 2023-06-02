package com.example.caxsecurityapp.entities;

public class Barrios {
    public String id;
    public String nombre;

    public Barrios(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Barrios() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
