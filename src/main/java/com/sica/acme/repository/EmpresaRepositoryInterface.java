package com.sica.acme.repository;

import com.sica.acme.model.Empresa;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface EmpresaRepositoryInterface {

    void crear(Empresa empresa) throws SQLException;

    List<Empresa> listarTodos() throws SQLException;

    Optional<Empresa> buscarPorId(int id) throws SQLException;

    boolean actualizar(Empresa empresa) throws SQLException;

    boolean eliminar(int id) throws SQLException;
}
