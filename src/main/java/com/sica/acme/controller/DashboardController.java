package com.sica.acme.controller;

import com.sica.acme.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
}