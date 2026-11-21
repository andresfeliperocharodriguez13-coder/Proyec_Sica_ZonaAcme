package com.sica.acme.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Button btnIngresar;

   @FXML
    private void handleLogin() {

    String usuario = txtUsuario.getText();
    String contrasena = txtContrasena.getText();

    if (usuario.isBlank() || contrasena.isBlank()) {

        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Datos incompletos");
        alerta.setHeaderText("No se puede iniciar sesión");
        alerta.setContentText("Debes completar todos los campos.");
        alerta.showAndWait();

        return;
    }

    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
    alerta.setTitle("SICA");
    alerta.setHeaderText("Formulario recibido");
    alerta.setContentText("Los datos fueron recibidos correctamente.");
    alerta.showAndWait();
}
}