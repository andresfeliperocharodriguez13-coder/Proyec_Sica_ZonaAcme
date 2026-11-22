package com.sica.acme.controller;

import com.sica.acme.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class DashboardController {
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblCorreo;
    @FXML
    private Label lblRol;
    public void setUsuario(Usuario usuario) {
        lblNombre.setText(usuario.getNombre());
        lblCorreo.setText(usuario.getEmail());
        lblRol.setText(usuario.getRol().getNombreRol());
    }
    @FXML
    private void handleCerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/sica/acme/view/login-view.fxml"
                    )
            );
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) lblNombre.getScene().getWindow();
            stage.setTitle("SICA - Zona Acme");
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}