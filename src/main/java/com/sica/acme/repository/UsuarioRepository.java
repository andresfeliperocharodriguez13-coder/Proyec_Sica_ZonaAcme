package com.sica.acme.repository;

import com.sica.acme.infrastructure.database.ConnectionFactory;
import com.sica.acme.model.Rol;
import com.sica.acme.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UsuarioRepository {
    public Optional<Usuario> buscarPorEmail(String email)
            throws SQLException {
        String sql = """
                SELECT u.id, u.nombre, u.email, u.password,
                       u.esta_activo, r.id AS rol_id,
                       r.nombre_rol
                FROM usuarios u
                INNER JOIN roles r ON u.rol_id = r.id
                WHERE u.email = ?
                """;
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Rol rol = new Rol(
                            resultSet.getInt("rol_id"),
                            resultSet.getString("nombre_rol")
                    );
                    Usuario usuario = new Usuario(
                            resultSet.getInt("id"),
                            resultSet.getString("nombre"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            rol,
                            resultSet.getBoolean("esta_activo")
                    );
                    return Optional.of(usuario);
                }
            }
        }
        return Optional.empty();
    }
}
