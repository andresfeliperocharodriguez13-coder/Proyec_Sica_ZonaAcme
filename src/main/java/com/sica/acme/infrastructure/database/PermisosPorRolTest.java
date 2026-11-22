package com.sica.acme.infrastructure.database;

import com.sica.acme.model.Permiso;
import com.sica.acme.repository.PermisoRepository;

import java.sql.SQLException;
import java.util.List;

public class PermisosPorRolTest {

    public static void main(String[] args) {
        PermisoRepository repository = new PermisoRepository();
        int rolId = 3;  // 3= Es el Guarda de Seguridad xd
        try {
            List<Permiso> permisos =
                    repository.listarPorRolId(rolId);
            System.out.println("Permisos del Guarda de Seguridad:");
            System.out.println("Total: " + permisos.size());
            for (Permiso permiso : permisos) {
                System.out.println("- " + permiso.getNombrePermiso());
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los permisos.");
            e.printStackTrace();
        }
    }
}
