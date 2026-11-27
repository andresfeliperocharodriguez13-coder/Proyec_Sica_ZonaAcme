package com.sica.acme.repository;

import com.sica.acme.model.Incidente;

import java.sql.SQLException;
import java.util.List;

public interface IncidenteRepositoryInterface {

    void crear(Incidente incidente) throws SQLException;

    List<Incidente> listarTodos() throws SQLException;
}
