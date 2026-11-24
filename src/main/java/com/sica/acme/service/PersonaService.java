package com.sica.acme.service;

import com.sica.acme.model.Persona;
import com.sica.acme.model.Usuario;
import com.sica.acme.repository.PersonaRepositoryInterface;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PersonaService {

    private final PersonaRepositoryInterface personaRepository;
    private final PermisoService permisoService;

    public PersonaService(
            PersonaRepositoryInterface personaRepository,
            PermisoService permisoService) {
        this.personaRepository =
                Objects.requireNonNull(personaRepository);
        this.permisoService =
                Objects.requireNonNull(permisoService);
    }

    public void crearPersona(Persona persona, Usuario usuario)
            throws SQLException {
        verificarPermiso(usuario, "crear_persona");
        validarPersona(persona);
        if (personaRepository.buscarPorDocumento(
                persona.getDocumentoIdentidad()).isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe una persona con ese documento.");
        }
        personaRepository.crear(persona);
    }
    public List<Persona> listarPersonas(Usuario usuario)
            throws SQLException {
        verificarPermiso(usuario, "consultar_personas");
        return personaRepository.listarTodos();
    }
    public Optional<Persona> buscarPorDocumento(
            String documento, Usuario usuario) throws SQLException {
        verificarPermiso(usuario, "consultar_personas");
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException(
                    "El documento no puede estar vacío.");
        }
        return personaRepository.buscarPorDocumento(
                documento.trim());
    }
    public boolean actualizarPersona(
            Persona persona, Usuario usuario) throws SQLException {
        verificarPermiso(usuario, "editar_persona");
        validarPersona(persona);
        Optional<Persona> existente =
                personaRepository.buscarPorDocumento(
                        persona.getDocumentoIdentidad());
        if (existente.isPresent()
                && existente.get().getId() != persona.getId()) {
            throw new IllegalArgumentException(
                    "El documento ya pertenece a otra persona.");
        }
        return personaRepository.actualizar(persona);
    }
    private void verificarPermiso(
            Usuario usuario, String nombrePermiso)
            throws SQLException {
        if (!permisoService.tienePermiso(usuario, nombrePermiso)) {
            throw new SecurityException(
                    "No tienes permiso para realizar esta operación.");
        }
    }

    private void validarPersona(Persona persona) {
        if (persona == null) {
            throw new IllegalArgumentException(
                    "La persona no puede ser nula.");
        }
        if (persona.getNombre() == null
                || persona.getNombre().isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre es obligatorio.");
        }
        if (persona.getDocumentoIdentidad() == null
                || persona.getDocumentoIdentidad().isBlank()) {
            throw new IllegalArgumentException(
                    "El documento de identidad es obligatorio.");
        }
        if (!"Trabajador".equals(persona.getTipoPersona())
                && !"Invitado".equals(persona.getTipoPersona())) {
            throw new IllegalArgumentException(
                    "El tipo de persona no es válido.");
        }
        if (persona.getEstadoAccesoId() <= 0) {
            throw new IllegalArgumentException(
                    "El estado de acceso es obligatorio.");
        }
    }
}