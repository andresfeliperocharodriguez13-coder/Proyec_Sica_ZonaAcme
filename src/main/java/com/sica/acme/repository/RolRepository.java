package com.sica.acme.repository;

import com.sica.acme.infrastructure.database.ConnectionFactory;
import com.sica.acme.model.Rol;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

public class RolRepository {
    public List<Rol> listarTodos() throws SQLException {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT id, nombre_rol FROM roles";
        try (Connection connection = ConnectionFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Rol rol = new Rol(
                        resultSet.getInt("id"),
                        resultSet.getString("nombre_rol")
                );
                roles.add(rol);
            }
        }
        return roles;
    }
}
