package com.sica.acme.infrastructure.database;

import com.sica.acme.model.Rol;
import com.sica.acme.repository.RolRepository;
import java.sql.SQLException;
import java.util.List;

public class RolRepositoryTest {

    public static void main(String[] args) {
        RolRepository repository = new RolRepository();
        try {
            List<Rol> roles = repository.listarTodos();

            System.out.println("Roles encontrados:");
            for (Rol rol : roles) {
                System.out.println(
                        rol.getId() + " - " + rol.getNombreRol()
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los roles.");
            e.printStackTrace();
        }
    }
}
