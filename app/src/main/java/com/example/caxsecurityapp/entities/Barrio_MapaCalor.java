package com.example.caxsecurityapp.entities;

public class Barrio_MapaCalor {

    public String id;
    public String nombre;
    public String estado;
    public int CantReportes;

    public Barrio_MapaCalor() {
    }

    public Barrio_MapaCalor(String id, String nombre, String estado, int cantReportes) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        CantReportes = cantReportes;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCantReportes() {
        return CantReportes;
    }

    public void setCantReportes(int cantReportes) {
        CantReportes = cantReportes;
    }
}
