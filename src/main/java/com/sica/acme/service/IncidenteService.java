package com.sica.acme.service;

import com.sica.acme.model.Incidente;
import com.sica.acme.model.Usuario;
import com.sica.acme.repository.IncidenteRepositoryInterface;

import java.sql.SQLException;
import java.util.List;

public class IncidenteService {

    private final IncidenteRepositoryInterface incidenteRepository;
    private final PermisoService permisoService;

    public IncidenteService(
            IncidenteRepositoryInterface incidenteRepository,
            PermisoService permisoService) {
        this.incidenteRepository = incidenteRepository;
        this.permisoService = permisoService;
    }

    public void registrarIncidente(Incidente incidente, Usuario usuario)
            throws SQLException {

        if (usuario == null || !usuario.isEstaActivo()) {
            throw new IllegalArgumentException(
                    "El usuario no está activo."
            );
        }

        if (!permisoService.tienePermiso(usuario, "registrar_incidente")) {
            throw new SecurityException(
                    "No tienes permiso para registrar incidentes."
            );
        }

        if (incidente == null ||
                incidente.getDescripcion() == null ||
                incidente.getDescripcion().isBlank()) {
            throw new IllegalArgumentException(
                    "La descripción del incidente es obligatoria."
            );
        }

        if (incidente.getVisitaId() != null &&
                incidente.getVisitaId() <= 0) {
            throw new IllegalArgumentException(
                    "El identificador de la visita no es válido."
            );
        }

        incidente.setReportadoPorId(usuario.getId());

        incidenteRepository.crear(incidente);
    }

    public List<Incidente> listarIncidentes(Usuario usuario)
            throws SQLException {

        if (usuario == null || !usuario.isEstaActivo()) {
            throw new IllegalArgumentException(
                    "El usuario no está activo."
            );
        }

        if (!permisoService.tienePermiso(usuario, "consultar_incidentes")) {
            throw new SecurityException(
                    "No tienes permiso para consultar incidentes."
            );
        }

        return incidenteRepository.listarTodos();
    }
}
