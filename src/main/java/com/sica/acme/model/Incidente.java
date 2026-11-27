package com.sica.acme.model;

import java.time.LocalDateTime;

public class Incidente {

    private int id;
    private Integer visitaId;
    private Integer reportadoPorId;
    private LocalDateTime fecha;
    private String descripcion;

    public Incidente() {
    }

    public Incidente(
            int id,
            Integer visitaId,
            Integer reportadoPorId,
            LocalDateTime fecha,
            String descripcion) {

        this.id = id;
        this.visitaId = visitaId;
        this.reportadoPorId = reportadoPorId;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getVisitaId() {
        return visitaId;
    }

    public void setVisitaId(Integer visitaId) {
        this.visitaId = visitaId;
    }

    public Integer getReportadoPorId() {
        return reportadoPorId;
    }

    public void setReportadoPorId(Integer reportadoPorId) {
        this.reportadoPorId = reportadoPorId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
