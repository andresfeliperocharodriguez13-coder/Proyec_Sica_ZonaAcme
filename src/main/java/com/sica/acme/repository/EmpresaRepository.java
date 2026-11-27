
package com.sica.acme.repository;

import com.sica.acme.infrastructure.database.ConnectionFactory;
import com.sica.acme.model.Empresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmpresaRepository implements EmpresaRepositoryInterface {

    @Override
    public void crear(Empresa empresa) throws SQLException {

        String sql = """
                INSERT INTO empresas (nombre, contacto_principal)
                VALUES (?, ?)
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, empresa.getNombre());
            statement.setString(2, empresa.getContactoPrincipal());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    empresa.setId(resultSet.getInt(1));
                }
            }
        }
    }

    @Override
    public List<Empresa> listarTodos() throws SQLException {

        List<Empresa> empresas = new ArrayList<>();

        String sql = """
                SELECT id, nombre, contacto_principal
                FROM empresas
                ORDER BY id
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                empresas.add(mapearEmpresa(resultSet));
            }
        }

        return empresas;
    }

    @Override
    public Optional<Empresa> buscarPorId(int id) throws SQLException {

        String sql = """
                SELECT id, nombre, contacto_principal
                FROM empresas
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapearEmpresa(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean actualizar(Empresa empresa) throws SQLException {

        String sql = """
                UPDATE empresas
                SET nombre = ?,
                    contacto_principal = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, empresa.getNombre());
            statement.setString(2, empresa.getContactoPrincipal());
            statement.setInt(3, empresa.getId());

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {

        String sql = """
                DELETE FROM empresas
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        }
    }

    private Empresa mapearEmpresa(ResultSet resultSet) throws SQLException {

        return new Empresa(
                resultSet.getInt("id"),
                resultSet.getString("nombre"),
                resultSet.getString("contacto_principal")
        );
    }
}