package com.sica.acme.repository;

import com.sica.acme.model.Persona;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PersonaRepositoryInterface {
    void crear(Persona persona) throws SQLException;

        List<Persona> listarTodos() throws SQLException;

        Optional<Persona> buscarPorDocumento(String documento) throws SQLException;
        boolean actualizar(Persona persona) throws SQLException;
}