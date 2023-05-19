package com.example.caxsecurityapp.entities;

public class SolicitudAmistad {
    public String idUsuarioRemitente;
    public String nombreUsuarioRemitente;
    public String correoUsuarioReceptor;

    public SolicitudAmistad() {
    }

    public String getIdUsuarioRemitente() {
        return idUsuarioRemitente;
    }

    public void setIdUsuarioRemitente(String idUsuarioRemitente) {
        this.idUsuarioRemitente = idUsuarioRemitente;
    }

    public String getCorreoUsuarioReceptor() {
        return correoUsuarioReceptor;
    }

    public void setCorreoUsuarioReceptor(String correoUsuarioReceptor) {
        this.correoUsuarioReceptor = correoUsuarioReceptor;
    }

    public String getNombreUsuarioReceptor() {
        return nombreUsuarioRemitente;
    }

    public void setNombreUsuarioReceptor(String nombreUsuarioReceptor) {
        this.nombreUsuarioRemitente = nombreUsuarioReceptor;
    }
}