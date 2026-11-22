package com.sica.acme.service;

import com.sica.acme.model.Usuario;
import com.sica.acme.repository.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import java.util.Optional;

public class AutenticacionService {
    private final UsuarioRepository usuarioRepository;
    public AutenticacionService() {
        this.usuarioRepository = new UsuarioRepository();
    }
    public Optional<Usuario> autenticar(String email, String contrasena)
            throws SQLException {
        if (email == null || email.isBlank()
                || contrasena == null || contrasena.isBlank()) {
            return Optional.empty();
        }
        Optional<Usuario> resultado =
                usuarioRepository.buscarPorEmail(email.trim());
        if (resultado.isEmpty()) {
            return Optional.empty();
        }
        Usuario usuario = resultado.get();
        if (!usuario.isEstaActivo()) {
            return Optional.empty();
        }
        boolean contrasenaValida = BCrypt.checkpw(
                contrasena,
                usuario.getPassword()
        );
        if (!contrasenaValida) {
            return Optional.empty();
        }
        return Optional.of(usuario);
    }
}

//9:22 Commit six