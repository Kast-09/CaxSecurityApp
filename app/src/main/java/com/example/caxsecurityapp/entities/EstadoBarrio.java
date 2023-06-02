package com.example.caxsecurityapp.entities;

public class EstadoBarrio {
    public String id;
    public int d1;
    public int d2;
    public int d3;

    public EstadoBarrio() {
    }

    public EstadoBarrio(String id, int d1, int d2, int d3) {
        this.id = id;
        this.d1 = d1;
        this.d2 = d2;
        this.d3 = d3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getD1() {
        return d1;
    }

    public void setD1(int d1) {
        this.d1 = d1;
    }

    public int getD2() {
        return d2;
    }

    public void setD2(int d2) {
        this.d2 = d2;
    }

    public int getD3() {
        return d3;
    }

    public void setD3(int d3) {
        this.d3 = d3;
    }
}
