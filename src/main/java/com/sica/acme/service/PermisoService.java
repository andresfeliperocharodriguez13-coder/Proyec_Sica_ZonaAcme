package com.sica.acme.service;

import com.sica.acme.model.Permiso;
import com.sica.acme.model.Usuario;
import com.sica.acme.repository.PermisoRepositoryInterface;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PermisoService {

    private final PermisoRepositoryInterface permisoRepository;
    public PermisoService(PermisoRepositoryInterface permisoRepository) {
        this.permisoRepository = Objects.requireNonNull(permisoRepository);
    }
    public boolean tienePermiso(Usuario usuario, String nombrePermiso)
            throws SQLException {
        return obtenerNombresPermisos(usuario).contains(nombrePermiso);
    }
    public Set<String> obtenerNombresPermisos(Usuario usuario)
            throws SQLException {
        if (usuario == null || usuario.getRol() == null
                || !usuario.isEstaActivo()) {
            return Set.of();
        }
        List<Permiso> permisos =
                permisoRepository.listarPorRolId(usuario.getRol().getId());
        return permisos.stream()
                .map(Permiso::getNombrePermiso)
                .collect(Collectors.toSet());
    }
}