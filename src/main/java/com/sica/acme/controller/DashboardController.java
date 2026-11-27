
package com.sica.acme.controller;

import com.sica.acme.model.Usuario;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblCorreo;

    @FXML
    private Label lblRol;

    @FXML
    private Label lblTituloContenido;

    @FXML
    private Label lblDescripcionContenido;

    @FXML
    private StackPane contenidoCentral;

    private Usuario usuario;

    private Node contenidoInicial;

    @FXML
    private void initialize() {
        contenidoInicial = contenidoCentral.getChildren().get(0);
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;

        lblNombre.setText(usuario.getNombre());
        lblCorreo.setText(usuario.getEmail());
        lblRol.setText(usuario.getRol().getNombreRol());
    }

    @FXML
    private void handlePanelPrincipal() {
        mostrarSeccion(
                "Bienvenido a SICA",
                "Sistema de control de acceso de Zona Acme."
        );
    }

    @FXML
    private void handleUsuarios() {
        mostrarSeccion(
                "Usuarios y roles",
                "Administra los usuarios del sistema y sus respectivos roles y permisos."
        );
    }

    @FXML
    private void handleEmpresas() {
        if (usuario == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/sica/acme/view/empresas-view.fxml"
                    )
            );

            Node vista = loader.load();

            EmpresaController controller = loader.getController();
            controller.setUsuario(usuario);

            contenidoCentral.getChildren().setAll(vista);

        } catch (IOException e) {
            mostrarAlerta(
                    "Error al abrir Empresas",
                    "No fue posible cargar la pantalla de empresas."
            );
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePersonas() {
        if (usuario == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/sica/acme/view/personas-view.fxml"
                    )
            );

            Node vista = loader.load();

            PersonaController controller = loader.getController();
            controller.setUsuario(usuario);

            contenidoCentral.getChildren().setAll(vista);

        } catch (IOException e) {
            mostrarAlerta(
                    "Error al abrir Personas",
                    "No fue posible cargar la pantalla de personas."
            );
            e.printStackTrace();
        }
    }
    @FXML
    private void handleVisitas() {

        if (usuario == null) {
            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/sica/acme/view/visitas-view.fxml"
                    )
            );

            Node vista = loader.load();

            VisitaController controller = loader.getController();

            controller.setUsuario(usuario);

            contenidoCentral.getChildren().setAll(vista);

        } catch (IOException e) {

            mostrarAlerta(
                    "Error",
                    "No fue posible cargar el módulo de visitas."
            );

            e.printStackTrace();
        }
    }

    @FXML
    private void handleAprobaciones() {
        mostrarSeccion(
                "Aprobaciones",
                "Revisa las solicitudes de acceso pendientes de aprobación."
        );
    }

    @FXML
    private void handleIncidentes() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/sica/acme/view/incidentes-view.fxml"
                    )
            );

            Node vista = loader.load();

            IncidenteController controller = loader.getController();
            controller.setUsuario(usuario);

            contenidoCentral.getChildren().setAll(vista);

        } catch (IOException e) {
            mostrarAlerta(
                    "Error",
                    "No se pudo cargar el módulo de incidentes: " + e.getMessage()
            );
        }
    }

    @FXML
    private void handleReportes() {
        mostrarSeccion(
                "Reportes",
                "Consulta información y reportes relacionados con el control de acceso."
        );
    }

    private void mostrarSeccion(
            String titulo,
            String descripcion) {

        contenidoCentral.getChildren().setAll(contenidoInicial);

        lblTituloContenido.setText(titulo);
        lblDescripcionContenido.setText(descripcion);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
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
