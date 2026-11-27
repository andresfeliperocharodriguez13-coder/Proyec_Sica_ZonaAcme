package com.sica.acme.controller;

import com.sica.acme.model.Incidente;
import com.sica.acme.model.Usuario;
import com.sica.acme.repository.IncidenteRepository;
import com.sica.acme.repository.PermisoRepository;
import com.sica.acme.service.IncidenteService;
import com.sica.acme.service.PermisoService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class IncidenteController {

    @FXML private TableView<Incidente> tablaIncidentes;

    @FXML private TableColumn<Incidente, String> colId;
    @FXML private TableColumn<Incidente, String> colVisita;
    @FXML private TableColumn<Incidente, String> colReportadoPor;
    @FXML private TableColumn<Incidente, String> colFecha;
    @FXML private TableColumn<Incidente, String> colDescripcion;

    @FXML private Button btnNuevoIncidente;
    @FXML private Button btnActualizar;

    private Usuario usuario;

    private final PermisoService permisoService =
            new PermisoService(new PermisoRepository());

    private final IncidenteService incidenteService =
            new IncidenteService(new IncidenteRepository(), permisoService);

    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        actualizarPermisos();
        configurarTabla();
        cargarIncidentes();
    }

    private void actualizarPermisos() {
        try {
            btnNuevoIncidente.setDisable(
                    !permisoService.tienePermiso(usuario, "registrar_incidente")
            );

            btnActualizar.setDisable(
                    !permisoService.tienePermiso(usuario, "consultar_incidentes")
            );

        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron consultar los permisos: "
                    + e.getMessage());
        }
    }

    private void configurarTabla() {
        colId.setCellValueFactory(data ->
                new SimpleStringProperty(
                        String.valueOf(data.getValue().getId())
                )
        );

        colVisita.setCellValueFactory(data -> {
            Integer visitaId = data.getValue().getVisitaId();

            return new SimpleStringProperty(
                    visitaId == null ? "Sin visita" : String.valueOf(visitaId)
            );
        });

        colReportadoPor.setCellValueFactory(data -> {
            Integer usuarioId = data.getValue().getReportadoPorId();

            return new SimpleStringProperty(
                    usuarioId == null ? "No disponible" : String.valueOf(usuarioId)
            );
        });

        colFecha.setCellValueFactory(data -> {
            if (data.getValue().getFecha() == null) {
                return new SimpleStringProperty("");
            }

            return new SimpleStringProperty(
                    data.getValue().getFecha().format(formatoFecha)
            );
        });

        colDescripcion.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDescripcion())
        );
    }

    private void cargarIncidentes() {
        try {
            List<Incidente> incidentes =
                    incidenteService.listarIncidentes(usuario);

            tablaIncidentes.setItems(
                    FXCollections.observableArrayList(incidentes)
            );

        } catch (SQLException | SecurityException | IllegalArgumentException e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }

    @FXML
    private void handleNuevoIncidente() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Registrar incidente");
        dialog.setHeaderText("Ingresa la información del incidente");

        ButtonType botonGuardar = new ButtonType("Guardar");
        dialog.getDialogPane().getButtonTypes().addAll(
                botonGuardar, ButtonType.CANCEL
        );

        TextField txtVisitaId = new TextField();
        txtVisitaId.setPromptText("ID de visita (opcional)");

        TextArea txtDescripcion = new TextArea();
        txtDescripcion.setPromptText("Describe lo ocurrido...");
        txtDescripcion.setPrefRowCount(5);
        txtDescripcion.setWrapText(true);

        GridPane contenido = new GridPane();
        contenido.setHgap(10);
        contenido.setVgap(10);

        contenido.add(new Label("ID de visita:"), 0, 0);
        contenido.add(txtVisitaId, 1, 0);
        contenido.add(new Label("Descripción:"), 0, 1);
        contenido.add(txtDescripcion, 1, 1);

        dialog.getDialogPane().setContent(contenido);
        dialog.getDialogPane().setPrefWidth(450);

        dialog.showAndWait().ifPresent(resultado -> {
            if (resultado == botonGuardar) {
                registrarIncidente(txtVisitaId.getText(),
                        txtDescripcion.getText());
            }
        });
    }

    private void registrarIncidente(String textoVisitaId, String descripcion) {
        try {
            Integer visitaId = null;

            if (textoVisitaId != null && !textoVisitaId.isBlank()) {
                visitaId = Integer.parseInt(textoVisitaId.trim());
            }

            Incidente incidente = new Incidente();
            incidente.setVisitaId(visitaId);
            incidente.setDescripcion(descripcion);

            incidenteService.registrarIncidente(incidente, usuario);

            mostrarAlerta("Éxito", "El incidente fue registrado correctamente.");
            cargarIncidentes();

        } catch (NumberFormatException e) {
            mostrarAlerta("Dato inválido",
                    "El ID de la visita debe ser un número entero.");

        } catch (SQLException | SecurityException | IllegalArgumentException e) {
            mostrarAlerta("No se pudo registrar el incidente", e.getMessage());
        }
    }

    @FXML
    private void handleActualizar() {
        cargarIncidentes();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
