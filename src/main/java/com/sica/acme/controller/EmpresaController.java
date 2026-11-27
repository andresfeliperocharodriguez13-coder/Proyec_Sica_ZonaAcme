
package com.sica.acme.controller;

import com.sica.acme.model.Empresa;
import com.sica.acme.model.Usuario;
import com.sica.acme.repository.EmpresaRepository;
import com.sica.acme.repository.PermisoRepository;
import com.sica.acme.service.EmpresaService;
import com.sica.acme.service.PermisoService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EmpresaController {

    @FXML
    private TableView<Empresa> tablaEmpresas;

    @FXML
    private TableColumn<Empresa, Integer> colId;

    @FXML
    private TableColumn<Empresa, String> colNombre;

    @FXML
    private TableColumn<Empresa, String> colContacto;

    private final EmpresaService empresaService =
            new EmpresaService(
                    new EmpresaRepository(),
                    new PermisoService(new PermisoRepository())
            );

    private Usuario usuario;

    @FXML
    private void initialize() {

        colId.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

        colContacto.setCellValueFactory(
                new PropertyValueFactory<>("contactoPrincipal")
        );
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarEmpresas();
    }

    private void cargarEmpresas() {

        if (usuario == null) {
            return;
        }

        try {
            List<Empresa> empresas =
                    empresaService.listarEmpresas(usuario);

            tablaEmpresas.setItems(
                    FXCollections.observableArrayList(empresas)
            );

        } catch (SecurityException e) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Acceso denegado",
                    e.getMessage()
            );

        } catch (SQLException e) {
            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error de conexión",
                    "No fue posible cargar las empresas."
            );
            e.printStackTrace();
        }
    }

    @FXML
    private void handleActualizar() {
        cargarEmpresas();
    }

    @FXML
    private void handleNuevaEmpresa() {

        Dialog<Empresa> dialog = new Dialog<>();

        dialog.setTitle("Nueva empresa");
        dialog.setHeaderText("Registrar una nueva empresa");

        ButtonType guardar = new ButtonType(
                "Guardar",
                ButtonBar.ButtonData.OK_DONE
        );

        ButtonType cancelar = new ButtonType(
                "Cancelar",
                ButtonBar.ButtonData.CANCEL_CLOSE
        );

        dialog.getDialogPane().getButtonTypes().addAll(
                guardar,
                cancelar
        );

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre de la empresa");

        TextField txtContacto = new TextField();
        txtContacto.setPromptText("Contacto principal");

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(12);
        formulario.setPadding(new Insets(20));

        formulario.add(new Label("Nombre:"), 0, 0);
        formulario.add(txtNombre, 1, 0);

        formulario.add(new Label("Contacto:"), 0, 1);
        formulario.add(txtContacto, 1, 1);

        dialog.getDialogPane().setContent(formulario);

        Button botonGuardar = (Button)
                dialog.getDialogPane().lookupButton(guardar);

        botonGuardar.addEventFilter(
                ActionEvent.ACTION,
                evento -> {
                    if (txtNombre.getText() == null
                            || txtNombre.getText().isBlank()) {

                        mostrarAlerta(
                                Alert.AlertType.WARNING,
                                "Campo obligatorio",
                                "Debes ingresar el nombre de la empresa."
                        );

                        evento.consume();
                    }
                }
        );

        dialog.setResultConverter(boton -> {

            if (boton == guardar) {

                String contacto = txtContacto.getText().trim();

                return new Empresa(
                        0,
                        txtNombre.getText().trim(),
                        contacto.isEmpty() ? null : contacto
                );
            }

            return null;
        });

        Optional<Empresa> resultado = dialog.showAndWait();

        resultado.ifPresent(empresa -> {

            try {
                empresaService.crearEmpresa(empresa, usuario);

                cargarEmpresas();

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Registro exitoso",
                        "La empresa se registró correctamente."
                );

            } catch (SecurityException e) {
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Acceso denegado",
                        e.getMessage()
                );

            } catch (IllegalArgumentException e) {
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Datos inválidos",
                        e.getMessage()
                );

            } catch (SQLException e) {
                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Error de base de datos",
                        "No fue posible registrar la empresa."
                );
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleEditarEmpresa() {

        Empresa empresaSeleccionada =
                tablaEmpresas.getSelectionModel().getSelectedItem();

        if (empresaSeleccionada == null) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Selección requerida",
                    "Selecciona una empresa de la tabla."
            );
            return;
        }

        Dialog<Empresa> dialog = new Dialog<>();

        dialog.setTitle("Editar empresa");
        dialog.setHeaderText("Modificar información de la empresa");

        ButtonType guardar = new ButtonType(
                "Guardar cambios",
                ButtonBar.ButtonData.OK_DONE
        );

        ButtonType cancelar = new ButtonType(
                "Cancelar",
                ButtonBar.ButtonData.CANCEL_CLOSE
        );

        dialog.getDialogPane().getButtonTypes().addAll(
                guardar,
                cancelar
        );

        TextField txtNombre = new TextField(
                empresaSeleccionada.getNombre()
        );

        String contactoActual =
                empresaSeleccionada.getContactoPrincipal() == null
                        ? ""
                        : empresaSeleccionada.getContactoPrincipal();

        TextField txtContacto = new TextField(contactoActual);

        txtNombre.setPromptText("Nombre de la empresa");
        txtContacto.setPromptText("Contacto principal");

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(12);
        formulario.setPadding(new Insets(20));

        formulario.add(new Label("Nombre:"), 0, 0);
        formulario.add(txtNombre, 1, 0);

        formulario.add(new Label("Contacto:"), 0, 1);
        formulario.add(txtContacto, 1, 1);

        dialog.getDialogPane().setContent(formulario);

        Button botonGuardar = (Button)
                dialog.getDialogPane().lookupButton(guardar);

        botonGuardar.addEventFilter(
                ActionEvent.ACTION,
                evento -> {
                    if (txtNombre.getText() == null
                            || txtNombre.getText().isBlank()) {

                        mostrarAlerta(
                                Alert.AlertType.WARNING,
                                "Campo obligatorio",
                                "Debes ingresar el nombre de la empresa."
                        );

                        evento.consume();
                    }
                }
        );

        dialog.setResultConverter(boton -> {

            if (boton == guardar) {

                String contacto = txtContacto.getText().trim();

                return new Empresa(
                        empresaSeleccionada.getId(),
                        txtNombre.getText().trim(),
                        contacto.isEmpty() ? null : contacto
                );
            }

            return null;
        });

        Optional<Empresa> resultado = dialog.showAndWait();

        resultado.ifPresent(empresa -> {

            try {
                boolean actualizado =
                        empresaService.actualizarEmpresa(empresa, usuario);

                if (actualizado) {

                    cargarEmpresas();

                    mostrarAlerta(
                            Alert.AlertType.INFORMATION,
                            "Actualización exitosa",
                            "La empresa se actualizó correctamente."
                    );

                } else {
                    mostrarAlerta(
                            Alert.AlertType.WARNING,
                            "Sin cambios",
                            "No se encontró la empresa para actualizar."
                    );
                }

            } catch (SecurityException e) {
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Acceso denegado",
                        e.getMessage()
                );

            } catch (IllegalArgumentException e) {
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Datos inválidos",
                        e.getMessage()
                );

            } catch (SQLException e) {
                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Error de base de datos",
                        "No fue posible actualizar la empresa."
                );
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleEliminarEmpresa() {

        Empresa empresaSeleccionada =
                tablaEmpresas.getSelectionModel().getSelectedItem();

        if (empresaSeleccionada == null) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Selección requerida",
                    "Selecciona una empresa de la tabla."
            );
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);

        confirmacion.setTitle("Eliminar empresa");
        confirmacion.setHeaderText("¿Deseas eliminar esta empresa?");
        confirmacion.setContentText(
                "Empresa: " + empresaSeleccionada.getNombre()
        );

        Optional<ButtonType> respuesta = confirmacion.showAndWait();

        if (respuesta.isEmpty()
                || respuesta.get() != ButtonType.OK) {
            return;
        }

        try {
            boolean eliminada = empresaService.eliminarEmpresa(
                    empresaSeleccionada.getId(),
                    usuario
            );

            if (eliminada) {

                cargarEmpresas();

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Eliminación exitosa",
                        "La empresa se eliminó correctamente."
                );

            } else {
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Empresa no encontrada",
                        "No se encontró la empresa seleccionada."
                );
            }

        } catch (SecurityException e) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Acceso denegado",
                    e.getMessage()
            );

        } catch (IllegalStateException e) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "No se puede eliminar",
                    e.getMessage()
            );

        } catch (IllegalArgumentException e) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Datos inválidos",
                    e.getMessage()
            );

        } catch (SQLException e) {
            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error de base de datos",
                    "No fue posible eliminar la empresa."
            );
            e.printStackTrace();
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