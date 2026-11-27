
package com.sica.acme.repository;

import com.sica.acme.model.Visita;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface VisitaRepositoryInterface {

    void crear(Visita visita) throws SQLException;

    List<Visita> listarTodos() throws SQLException;

    Optional<Visita> buscarPorId(int id) throws SQLException;

    boolean actualizar(Visita visita) throws SQLException;
}
