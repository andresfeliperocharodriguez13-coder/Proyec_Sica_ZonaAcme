package com.sica.acme.model;
public class Persona {

    private int id;
    private String nombre;
    private String documentoIdentidad;
    private Integer empresaId;
    private String tipoPersona;
    private int estadoAccesoId;
    private String urlFoto;

    public Persona() {
    }
    public Persona(int id, String nombre, String documentoIdentidad,
                   Integer empresaId, String tipoPersona,
                   int estadoAccesoId, String urlFoto) {
        this.id = id;
        this.nombre = nombre;
        this.documentoIdentidad = documentoIdentidad;
        this.empresaId = empresaId;
        this.tipoPersona = tipoPersona;
        this.estadoAccesoId = estadoAccesoId;
        this.urlFoto = urlFoto;
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
    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }
    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }
    public Integer getEmpresaId() {
        return empresaId;
    }
    public void setEmpresaId(Integer empresaId) {
        this.empresaId = empresaId;
    }
    public String getTipoPersona() {
        return tipoPersona;
    }
    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }
    public int getEstadoAccesoId() {
        return estadoAccesoId;
    }
    public void setEstadoAccesoId(int estadoAccesoId) {
        this.estadoAccesoId = estadoAccesoId;
    }
    public String getUrlFoto() {
        return urlFoto;
    }
    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }
}