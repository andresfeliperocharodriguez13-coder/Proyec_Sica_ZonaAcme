package com.sica.acme.repository;

import com.sica.acme.infrastructure.database.ConnectionFactory;
import com.sica.acme.model.Persona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonaRepository implements PersonaRepositoryInterface {

    @Override
    public void crear(Persona persona) throws SQLException {
        String sql = """
                INSERT INTO personas
                (nombre, documento_identidad, empresa_id,
                 tipo_persona, estado_acceso_id, url_foto)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, persona.getNombre());
            statement.setString(2, persona.getDocumentoIdentidad());
            if (persona.getEmpresaId() == null) {
                statement.setNull(3, java.sql.Types.INTEGER);
            } else {
                statement.setInt(3, persona.getEmpresaId());
            }
            statement.setString(4, persona.getTipoPersona());
            statement.setInt(5, persona.getEstadoAccesoId());
            statement.setString(6, persona.getUrlFoto());
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    persona.setId(resultSet.getInt(1));
                }
            }
        }
    }

    @Override
    public List<Persona> listarTodos() throws SQLException {
        List<Persona> personas = new ArrayList<>();
        String sql = """
                SELECT id, nombre, documento_identidad, empresa_id,
                       tipo_persona, estado_acceso_id, url_foto
                FROM personas
                ORDER BY id
                """;
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                personas.add(mapearPersona(resultSet));
            }
        }
        return personas;
    }

    @Override
    public Optional<Persona> buscarPorDocumento(String documento)
            throws SQLException {
        String sql = """
                SELECT id, nombre, documento_identidad, empresa_id,
                       tipo_persona, estado_acceso_id, url_foto
                FROM personas
                WHERE documento_identidad = ?
                """;
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, documento);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapearPersona(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean actualizar(Persona persona) throws SQLException {
        String sql = """
                UPDATE personas
                SET nombre = ?,
                    documento_identidad = ?,
                    empresa_id = ?,
                    tipo_persona = ?,
                    estado_acceso_id = ?,
                    url_foto = ?
                WHERE id = ?
                """;
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, persona.getNombre());
            statement.setString(2, persona.getDocumentoIdentidad());
            if (persona.getEmpresaId() == null) {
                statement.setNull(3, java.sql.Types.INTEGER);
            } else {
                statement.setInt(3, persona.getEmpresaId());
            }
            statement.setString(4, persona.getTipoPersona());
            statement.setInt(5, persona.getEstadoAccesoId());
            statement.setString(6, persona.getUrlFoto());
            statement.setInt(7, persona.getId());
            return statement.executeUpdate() > 0;
        }
    }
    private Persona mapearPersona(ResultSet resultSet)
            throws SQLException {
        int empresaId = resultSet.getInt("empresa_id");
        Integer empresa = resultSet.wasNull() ? null : empresaId;
        return new Persona(
                resultSet.getInt("id"),
                resultSet.getString("nombre"),
                resultSet.getString("documento_identidad"),
                empresa,
                resultSet.getString("tipo_persona"),
                resultSet.getInt("estado_acceso_id"),
                resultSet.getString("url_foto")
        );
    }
}