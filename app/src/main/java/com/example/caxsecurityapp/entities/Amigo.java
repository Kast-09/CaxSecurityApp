package com.example.caxsecurityapp.entities;

public class Amigo {
    public String idUsuario;
    public String idAmigo;
    public String nombreAmigo;
    public String fotoUsuario;

    public Amigo() {
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdAmigo() {
        return idAmigo;
    }

    public void setIdAmigo(String idAmigo) {
        this.idAmigo = idAmigo;
    }

    public String getNombreAmigo() {
        return nombreAmigo;
    }

    public void setNombreAmigo(String nombreAmigo) {
        this.nombreAmigo = nombreAmigo;
    }

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }
}
