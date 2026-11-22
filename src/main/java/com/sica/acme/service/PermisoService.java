package com.sica.acme.service;

import com.sica.acme.model.Permiso;
import com.sica.acme.model.Usuario;
import com.sica.acme.repository.PermisoRepository;

import java.sql.SQLException;
import java.util.List;

public class PermisoService {
    private final PermisoRepository permisoRepository;
    public PermisoService() {
        this.permisoRepository = new PermisoRepository();
    }
    public boolean tienePermiso(Usuario usuario, String nombrePermiso)
            throws SQLException {
        if (usuario == null || usuario.getRol() == null
                || !usuario.isEstaActivo()) {
            return false;
        }
        List<Permiso> permisos =
                permisoRepository.listarPorRolId(usuario.getRol().getId());
        return permisos.stream()
                .anyMatch(permiso ->
                        permiso.getNombrePermiso().equals(nombrePermiso));
    }
}

//5:08
