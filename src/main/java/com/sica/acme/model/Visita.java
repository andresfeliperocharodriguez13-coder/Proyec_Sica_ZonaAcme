package com.sica.acme.model;

import java.time.LocalDateTime;

public class Visita {
    private int id;
    private int personaId;
    private Integer anfitrionId;
    private LocalDateTime fechaProgramada;
    private LocalDateTime fechaEntrada;
    private LocalDateTime fechaSalida;
    private int estadoVisitaId;
    private String vehiculoPlaca;
    private Integer visitaAprobadaPor;

    public Visita() {
    }

    public Visita(int id, int personaId, Integer anfitrionId,
                  LocalDateTime fechaProgramada,
                  LocalDateTime fechaEntrada,
                  LocalDateTime fechaSalida,
                  int estadoVisitaId, String vehiculoPlaca,
                  Integer visitaAprobadaPor) {
        this.id = id;
        this.personaId = personaId;
        this.anfitrionId = anfitrionId;
        this.fechaProgramada = fechaProgramada;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.estadoVisitaId = estadoVisitaId;
        this.vehiculoPlaca = vehiculoPlaca;
        this.visitaAprobadaPor = visitaAprobadaPor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonaId() {
        return personaId;
    }

    public void setPersonaId(int personaId) {
        this.personaId = personaId;
    }

    public Integer getAnfitrionId() {
        return anfitrionId;
    }

    public void setAnfitrionId(Integer anfitrionId) {
        this.anfitrionId = anfitrionId;
    }

    public LocalDateTime getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(LocalDateTime fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public LocalDateTime getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDateTime fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public int getEstadoVisitaId() {
        return estadoVisitaId;
    }

    public void setEstadoVisitaId(int estadoVisitaId) {
        this.estadoVisitaId = estadoVisitaId;
    }

    public String getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    public Integer getVisitaAprobadaPor() {
        return visitaAprobadaPor;
    }

    public void setVisitaAprobadaPor(Integer visitaAprobadaPor) {
        this.visitaAprobadaPor = visitaAprobadaPor;
    }
}