
package com.sica.acme.repository;

import com.sica.acme.infrastructure.database.ConnectionFactory;
import com.sica.acme.model.Visita;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VisitaRepository implements VisitaRepositoryInterface {

    @Override
    public void crear(Visita visita) throws SQLException {

        String sql = """
                INSERT INTO visitas
                (persona_id, anfitrion_id, fecha_programada,
                 fecha_entrada, fecha_salida, estado_visita_id,
                 vehiculo_placa, visita_aprobada_por)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, visita.getPersonaId());

            if (visita.getAnfitrionId() == null) {
                statement.setNull(2, java.sql.Types.INTEGER);
            } else {
                statement.setInt(2, visita.getAnfitrionId());
            }

            statement.setTimestamp(
                    3, Timestamp.valueOf(visita.getFechaProgramada()));

            if (visita.getFechaEntrada() == null) {
                statement.setNull(4, java.sql.Types.TIMESTAMP);
            } else {
                statement.setTimestamp(
                        4, Timestamp.valueOf(visita.getFechaEntrada()));
            }

            if (visita.getFechaSalida() == null) {
                statement.setNull(5, java.sql.Types.TIMESTAMP);
            } else {
                statement.setTimestamp(
                        5, Timestamp.valueOf(visita.getFechaSalida()));
            }

            statement.setInt(6, visita.getEstadoVisitaId());
            statement.setString(7, visita.getVehiculoPlaca());

            if (visita.getVisitaAprobadaPor() == null) {
                statement.setNull(8, java.sql.Types.INTEGER);
            } else {
                statement.setInt(8, visita.getVisitaAprobadaPor());
            }

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    visita.setId(resultSet.getInt(1));
                }
            }
        }
    }

    @Override
    public List<Visita> listarTodos() throws SQLException {

        List<Visita> visitas = new ArrayList<>();

        String sql = """
                SELECT id, persona_id, anfitrion_id,
                       fecha_programada, fecha_entrada,
                       fecha_salida, estado_visita_id,
                       vehiculo_placa, visita_aprobada_por
                FROM visitas
                ORDER BY fecha_programada DESC
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                visitas.add(mapearVisita(resultSet));
            }
        }

        return visitas;
    }

    @Override
    public Optional<Visita> buscarPorId(int id) throws SQLException {

        String sql = """
                SELECT id, persona_id, anfitrion_id,
                       fecha_programada, fecha_entrada,
                       fecha_salida, estado_visita_id,
                       vehiculo_placa, visita_aprobada_por
                FROM visitas
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapearVisita(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean actualizar(Visita visita) throws SQLException {

        String sql = """
                UPDATE visitas
                SET persona_id = ?,
                    anfitrion_id = ?,
                    fecha_programada = ?,
                    fecha_entrada = ?,
                    fecha_salida = ?,
                    estado_visita_id = ?,
                    vehiculo_placa = ?,
                    visita_aprobada_por = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, visita.getPersonaId());

            if (visita.getAnfitrionId() == null) {
                statement.setNull(2, java.sql.Types.INTEGER);
            } else {
                statement.setInt(2, visita.getAnfitrionId());
            }

            statement.setTimestamp(
                    3, Timestamp.valueOf(visita.getFechaProgramada()));

            if (visita.getFechaEntrada() == null) {
                statement.setNull(4, java.sql.Types.TIMESTAMP);
            } else {
                statement.setTimestamp(
                        4, Timestamp.valueOf(visita.getFechaEntrada()));
            }

            if (visita.getFechaSalida() == null) {
                statement.setNull(5, java.sql.Types.TIMESTAMP);
            } else {
                statement.setTimestamp(
                        5, Timestamp.valueOf(visita.getFechaSalida()));
            }

            statement.setInt(6, visita.getEstadoVisitaId());
            statement.setString(7, visita.getVehiculoPlaca());

            if (visita.getVisitaAprobadaPor() == null) {
                statement.setNull(8, java.sql.Types.INTEGER);
            } else {
                statement.setInt(8, visita.getVisitaAprobadaPor());
            }

            statement.setInt(9, visita.getId());

            return statement.executeUpdate() > 0;
        }
    }

    private Visita mapearVisita(ResultSet resultSet)
            throws SQLException {

        int anfitrionId = resultSet.getInt("anfitrion_id");
        Integer anfitrion = resultSet.wasNull() ? null : anfitrionId;

        Timestamp fechaProgramada =
                resultSet.getTimestamp("fecha_programada");

        Timestamp fechaEntrada =
                resultSet.getTimestamp("fecha_entrada");

        Timestamp fechaSalida =
                resultSet.getTimestamp("fecha_salida");

        int aprobadoPorId =
                resultSet.getInt("visita_aprobada_por");

        Integer aprobadoPor =
                resultSet.wasNull() ? null : aprobadoPorId;

        return new Visita(
                resultSet.getInt("id"),
                resultSet.getInt("persona_id"),
                anfitrion,
                fechaProgramada.toLocalDateTime(),
                fechaEntrada == null
                        ? null : fechaEntrada.toLocalDateTime(),
                fechaSalida == null
                        ? null : fechaSalida.toLocalDateTime(),
                resultSet.getInt("estado_visita_id"),
                resultSet.getString("vehiculo_placa"),
                aprobadoPor
        );
    }
}