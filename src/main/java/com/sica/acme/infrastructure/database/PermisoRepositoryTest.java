package com.sica.acme.infrastructure.database;

import com.sica.acme.model.Permiso;
import com.sica.acme.repository.PermisoRepository;

import java.sql.SQLException;
import java.util.List;

public class PermisoRepositoryTest {

    public static void main(String[] args) {

        PermisoRepository repository = new PermisoRepository();
        try {
            List<Permiso> permisos = repository.listarTodos();
            System.out.println("Permisos encontrados: "
                    + permisos.size());
            for (Permiso permiso : permisos) {
                System.out.println(
                        permiso.getId() + " - "
                        + permiso.getNombrePermiso()
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los permisos.");
            e.printStackTrace();
        }
    }
}
