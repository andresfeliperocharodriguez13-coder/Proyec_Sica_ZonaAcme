package com.sica.acme.controller;

import com.sica.acme.model.Usuario;
import com.sica.acme.service.AutenticacionService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
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
                abrirDashboard(resultado.get());
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

        } catch (IOException e) {
            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error de pantalla",
                    "No fue posible abrir el panel principal."
            );
            e.printStackTrace();

        } finally {
            btnIngresar.setDisable(false);
        }
    }

    private void abrirDashboard(Usuario usuario) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/sica/acme/view/dashboard-view.fxml"
                )
        );
        Scene scene = new Scene(loader.load());
        DashboardController controller = loader.getController();
        controller.setUsuario(usuario);
        Stage stage = (Stage) btnIngresar.getScene().getWindow();
        stage.setTitle("SICA - Panel principal");
        stage.setScene(scene);
        stage.centerOnScreen();
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