package com.sica.acme.repository;

import com.sica.acme.model.Permiso;

import java.sql.SQLException;
import java.util.List;

public interface PermisoRepositoryInterface {

    List<Permiso> listarTodos() throws SQLException;

    List<Permiso> listarPorRolId(int rolId) throws SQLException;
}