package com.example.caxsecurityapp.entities;

public class SolicitudAmistad {
    public String idUsuarioRemitente;
    public String nombreUsuarioRemitente;
    public String correoUsuarioReceptor;
    public String fotoUsuario;

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

    public String getNombreUsuarioRemitente() {
        return nombreUsuarioRemitente;
    }

    public void setNombreUsuarioRemitente(String nombreUsuarioRemitente) {
        this.nombreUsuarioRemitente = nombreUsuarioRemitente;
    }

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }
}
