
package com.sica.acme.service;

import com.sica.acme.model.Usuario;
import com.sica.acme.model.Visita;
import com.sica.acme.repository.VisitaRepositoryInterface;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class VisitaService {

    private final VisitaRepositoryInterface visitaRepository;
    private final PermisoService permisoService;

    public VisitaService(
            VisitaRepositoryInterface visitaRepository,
            PermisoService permisoService) {

        this.visitaRepository = Objects.requireNonNull(visitaRepository);
        this.permisoService = Objects.requireNonNull(permisoService);
    }

    // Registrar una visita
    public void registrarVisita(Visita visita, Usuario usuario)
            throws SQLException {

        verificarPermiso(usuario, "registrar_visita");
        validarVisita(visita);

        boolean puedeAprobar =
                permisoService.tienePermiso(usuario, "aprobar_visita");

        if (puedeAprobar) {
            visita.setEstadoVisitaId(4);
            visita.setVisitaAprobadaPor(usuario.getId());
        } else {
            visita.setEstadoVisitaId(3);
            visita.setVisitaAprobadaPor(null);
        }

        visita.setFechaEntrada(null);
        visita.setFechaSalida(null);

        visitaRepository.crear(visita);
    }

    // Listar todas las visitas
    public List<Visita> listarVisitas(Usuario usuario)
            throws SQLException {

        verificarPermiso(usuario, "consultar_visitas");

        return visitaRepository.listarTodos();
    }

    // Buscar una visita por su ID
    public Optional<Visita> buscarPorId(int id, Usuario usuario)
            throws SQLException {

        verificarPermiso(usuario, "consultar_visitas");

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El ID de la visita no es válido.");
        }

        return visitaRepository.buscarPorId(id);
    }

    // Aprobar una visita pendiente
    public void aprobarVisita(int id, Usuario usuario)
            throws SQLException {

        verificarPermiso(usuario, "aprobar_visita");

        Visita visita = obtenerVisita(id);

        if (visita.getEstadoVisitaId() != 3
                && visita.getEstadoVisitaId() != 8) {

            throw new IllegalStateException(
                    "La visita no está pendiente de aprobación.");
        }

        visita.setEstadoVisitaId(4);
        visita.setVisitaAprobadaPor(usuario.getId());

        visitaRepository.actualizar(visita);
    }

    // Rechazar una visita pendiente
    public void rechazarVisita(int id, Usuario usuario)
            throws SQLException {

        verificarPermiso(usuario, "rechazar_visita");

        Visita visita = obtenerVisita(id);

        if (visita.getEstadoVisitaId() != 3
                && visita.getEstadoVisitaId() != 8) {

            throw new IllegalStateException(
                    "La visita no está pendiente de aprobación.");
        }

        visita.setEstadoVisitaId(5);
        visita.setVisitaAprobadaPor(null);

        visitaRepository.actualizar(visita);
    }

    // Registrar la entrada de una persona
    public void registrarEntrada(int id, Usuario usuario)
            throws SQLException {

        verificarPermiso(usuario, "registrar_entrada");

        Visita visita = obtenerVisita(id);

        if (visita.getEstadoVisitaId() != 4) {
            throw new IllegalStateException(
                    "La visita debe estar aprobada para registrar la entrada.");
        }

        visita.setEstadoVisitaId(1);
        visita.setFechaEntrada(LocalDateTime.now());

        visitaRepository.actualizar(visita);
    }

    // Registrar la salida de una persona
    public void registrarSalida(int id, Usuario usuario)
            throws SQLException {

        verificarPermiso(usuario, "registrar_salida");

        Visita visita = obtenerVisita(id);

        if (visita.getEstadoVisitaId() != 1) {
            throw new IllegalStateException(
                    "La persona no tiene una visita activa dentro de la empresa.");
        }

        visita.setEstadoVisitaId(2);
        visita.setFechaSalida(LocalDateTime.now());

        visitaRepository.actualizar(visita);
    }

    // Obtener una visita existente
    private Visita obtenerVisita(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El ID de la visita no es válido.");
        }

        return visitaRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró la visita."));
    }

    // Comprobar permisos del usuario
    private void verificarPermiso(
            Usuario usuario,
            String nombrePermiso) throws SQLException {

        if (usuario == null || !usuario.isEstaActivo()) {
            throw new SecurityException(
                    "El usuario no está activo o no ha iniciado sesión.");
        }

        if (!permisoService.tienePermiso(usuario, nombrePermiso)) {
            throw new SecurityException(
                    "No tienes permiso para realizar esta operación.");
        }
    }

    // Validar los datos de la visita
    private void validarVisita(Visita visita) {

        if (visita == null) {
            throw new IllegalArgumentException(
                    "La visita no puede estar vacía.");
        }

        if (visita.getPersonaId() <= 0) {
            throw new IllegalArgumentException(
                    "Debes seleccionar una persona.");
        }

        if (visita.getFechaProgramada() == null) {
            throw new IllegalArgumentException(
                    "Debes indicar la fecha programada.");
        }

        String placa = visita.getVehiculoPlaca();

        if (placa != null && placa.length() > 10) {
            throw new IllegalArgumentException(
                    "La placa del vehículo no puede superar 10 caracteres.");
        }
    }
}
