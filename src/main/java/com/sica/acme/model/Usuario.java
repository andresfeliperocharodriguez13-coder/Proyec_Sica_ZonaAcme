package com.sica.acme.model;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String password;
    private Rol rol;
    private boolean estaActivo;

    public Usuario() {
    }
    public Usuario(int id, String nombre, String email,
                   String password, Rol rol, boolean estaActivo) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.estaActivo = estaActivo;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Rol getRol() {
        return rol;
    }
    public void setRol(Rol rol) {
        this.rol = rol;
    }
    public boolean isEstaActivo() {
        return estaActivo;
    }
    public void setEstaActivo(boolean estaActivo) {
        this.estaActivo = estaActivo;
    }
}
