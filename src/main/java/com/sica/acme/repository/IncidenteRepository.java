package com.sica.acme.repository;

import com.sica.acme.infrastructure.database.ConnectionFactory;
import com.sica.acme.model.Incidente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncidenteRepository implements IncidenteRepositoryInterface {

    @Override
    public void crear(Incidente incidente) throws SQLException {
        String sql = """
                INSERT INTO incidentes
                (visita_id, reportado_por_id, descripcion)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            if (incidente.getVisitaId() == null) {
                statement.setNull(1, Types.INTEGER);
            } else {
                statement.setInt(1, incidente.getVisitaId());
            }

            if (incidente.getReportadoPorId() == null) {
                statement.setNull(2, Types.INTEGER);
            } else {
                statement.setInt(2, incidente.getReportadoPorId());
            }

            statement.setString(3, incidente.getDescripcion());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    incidente.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public List<Incidente> listarTodos() throws SQLException {
        String sql = """
                SELECT id, visita_id, reportado_por_id, fecha, descripcion
                FROM incidentes
                ORDER BY fecha DESC
                """;

        List<Incidente> incidentes = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                int visitaId = result.getInt("visita_id");
                Integer visita = result.wasNull() ? null : visitaId;

                int reportadoPorId = result.getInt("reportado_por_id");
                Integer reportante = result.wasNull() ? null : reportadoPorId;

                Timestamp fecha = result.getTimestamp("fecha");

                Incidente incidente = new Incidente(
                        result.getInt("id"),
                        visita,
                        reportante,
                        fecha != null ? fecha.toLocalDateTime() : null,
                        result.getString("descripcion")
                );

                incidentes.add(incidente);
            }
        }

        return incidentes;
    }
}
