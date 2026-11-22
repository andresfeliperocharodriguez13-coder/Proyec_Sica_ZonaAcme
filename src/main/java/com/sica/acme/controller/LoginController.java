package com.sica.acme.controller;

import com.sica.acme.model.Usuario;
import com.sica.acme.service.AutenticacionService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import java.util.Optional;

public class LoginController {
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private Button btnIngresar;
    private final AutenticacionService autenticacionService =
            new AutenticacionService();
    @FXML
    private void handleLogin() {
        String email = txtUsuario.getText();
        String contrasena = txtContrasena.getText();
        if (email == null || email.isBlank()
                || contrasena == null || contrasena.isBlank()) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Datos incompletos",
                    "Debes completar todos los campos."
            );
            return;
        }
        btnIngresar.setDisable(true);
        try {
            Optional<Usuario> resultado =
                    autenticacionService.autenticar(email, contrasena);
            if (resultado.isPresent()) {
                Usuario usuario = resultado.get();
                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Bienvenido a SICA",
                        "Inicio de sesión correcto. Rol: "
                                + usuario.getRol().getNombreRol()
                );
            } else {
                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Acceso denegado",
                        "Correo o contraseña incorrectos, o cuenta inactiva."
                );
            }
        } catch (SQLException e) {
            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error de conexión",
                    "No fue posible consultar la base de datos."
            );
            e.printStackTrace();
        } finally {
            btnIngresar.setDisable(false);
        }
    }
    private void mostrarAlerta(
            Alert.AlertType tipo,
            String titulo,
            String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}