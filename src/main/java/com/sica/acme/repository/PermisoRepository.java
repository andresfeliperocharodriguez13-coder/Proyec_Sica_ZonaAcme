package com.sica.acme.repository;

import com.sica.acme.infrastructure.database.ConnectionFactory;
import com.sica.acme.model.Permiso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PermisoRepository {
    public List<Permiso> listarTodos() throws SQLException {
        List<Permiso> permisos = new ArrayList<>();
        String sql = """
                SELECT id, nombre_permiso, descripcion
                FROM permisos
                ORDER BY id
                """;
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Permiso permiso = new Permiso(
                        resultSet.getInt("id"),
                        resultSet.getString("nombre_permiso"),
                        resultSet.getString("descripcion")
                );
                permisos.add(permiso);
            }
        }
        return permisos;
    }
    public List<Permiso> listarPorRolId(int rolId) throws SQLException {
        List<Permiso> permisos = new ArrayList<>();
        String sql = """
                SELECT p.id, p.nombre_permiso, p.descripcion
                FROM permisos p
                INNER JOIN rol_permisos rp ON p.id = rp.permiso_id
                WHERE rp.rol_id = ?
                ORDER BY p.id
                """;
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, rolId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Permiso permiso = new Permiso(
                            resultSet.getInt("id"),
                            resultSet.getString("nombre_permiso"),
                            resultSet.getString("descripcion")
                    );
                    permisos.add(permiso);
                }
            }
        }
        return permisos;
    }
}
