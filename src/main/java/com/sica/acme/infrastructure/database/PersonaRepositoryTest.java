package com.sica.acme.infrastructure.database;

import com.sica.acme.model.Persona;
import com.sica.acme.repository.PersonaRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PersonaRepositoryTest {
    public static void main(String[] args) {
        PersonaRepository repository = new PersonaRepository();
        try {
            // para consultar personas registradas
            List<Persona> personas = repository.listarTodos();
            System.out.println("Personas encontradas: "
                    + personas.size());
            for (Persona persona : personas) {
                System.out.println(
                        persona.getId() + " - "
                        + persona.getNombre() + " - "
                        + persona.getDocumentoIdentidad()
                );
            }
            // Buscar una persona por documento
            Optional<Persona> resultado =
                    repository.buscarPorDocumento("100000001");
            if (resultado.isPresent()) {
                System.out.println("Persona encontrada: "
                        + resultado.get().getNombre());
            } else {
                System.out.println(
                        "No existe una persona con ese documento.");
            }
        } catch (SQLException e) {
            System.out.println(
                    "Error al consultar las personas.");
            e.printStackTrace();
        }
    }
}