package com.sica.acme.model;

public class Permiso {
    private int id;
    private String nombrePermiso;
    private String descripcion;
    public Permiso() {
    }
    public Permiso(int id, String nombrePermiso, String descripcion) {
        this.id = id;
        this.nombrePermiso = nombrePermiso;
        this.descripcion = descripcion;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombrePermiso() {
        return nombrePermiso;
    }
    public void setNombrePermiso(String nombrePermiso) {
        this.nombrePermiso = nombrePermiso;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
