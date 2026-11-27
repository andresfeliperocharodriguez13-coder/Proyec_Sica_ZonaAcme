
package com.sica.acme.controller;

import com.sica.acme.model.Empresa;
import com.sica.acme.model.Persona;
import com.sica.acme.model.Usuario;
import com.sica.acme.repository.EmpresaRepository;
import com.sica.acme.repository.PermisoRepository;
import com.sica.acme.repository.PersonaRepository;
import com.sica.acme.service.EmpresaService;
import com.sica.acme.service.PermisoService;
import com.sica.acme.service.PersonaService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PersonaController {

    @FXML
    private TableView<Persona> tablaPersonas;

    @FXML
    private TableColumn<Persona, Integer> colId;

    @FXML
    private TableColumn<Persona, String> colNombre;

    @FXML
    private TableColumn<Persona, String> colDocumento;

    @FXML
    private TableColumn<Persona, String> colTipo;

    @FXML
    private TableColumn<Persona, String> colEmpresa;

    @FXML
    private TableColumn<Persona, String> colEstado;

    private final PersonaService personaService =
            new PersonaService(
                    new PersonaRepository(),
                    new PermisoService(new PermisoRepository())
            );

    private final EmpresaService empresaService =
            new EmpresaService(
                    new EmpresaRepository(),
                    new PermisoService(new PermisoRepository())
            );

    private Usuario usuario;

    private List<Empresa> empresasDisponibles = List.of();

    private final Map<Integer, String> nombresEmpresas =
            new HashMap<>();

    @FXML
    private void initialize() {

        colId.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

        colDocumento.setCellValueFactory(
                new PropertyValueFactory<>("documentoIdentidad")
        );

        colTipo.setCellValueFactory(
                new PropertyValueFactory<>("tipoPersona")
        );

        colEmpresa.setCellValueFactory(
                celda -> new ReadOnlyStringWrapper(
                        obtenerNombreEmpresa(
                                celda.getValue().getEmpresaId()
                        )
                )
        );

        colEstado.setCellValueFactory(
                celda -> new ReadOnlyStringWrapper(
                        obtenerNombreEstado(
                                celda.getValue().getEstadoAccesoId()
                        )
                )
        );
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;

        cargarEmpresasDisponibles();
        cargarPersonas();
    }

    private void cargarEmpresasDisponibles() {

        if (usuario == null) {
            return;
        }

        try {
            empresasDisponibles =
                    empresaService.listarEmpresas(usuario);

            nombresEmpresas.clear();

            for (Empresa empresa : empresasDisponibles) {
                nombresEmpresas.put(
                        empresa.getId(),
                        empresa.getNombre()
                );
            }

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

    private void cargarPersonas() {

        if (usuario == null) {
            return;
        }

        try {
            List<Persona> personas =
                    personaService.listarPersonas(usuario);

            tablaPersonas.setItems(
                    FXCollections.observableArrayList(personas)
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
                    "No fue posible cargar las personas."
            );
            e.printStackTrace();
        }
    }

    @FXML
    private void handleActualizar() {
        cargarEmpresasDisponibles();
        cargarPersonas();
    }

    @FXML
    private void handleNuevaPersona() {

        Dialog<Persona> dialog = new Dialog<>();

        dialog.setTitle("Nueva persona");
        dialog.setHeaderText("Registrar una nueva persona");

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
        txtNombre.setPromptText("Nombre completo");

        TextField txtDocumento = new TextField();
        txtDocumento.setPromptText("Documento de identidad");

        ComboBox<String> cbTipo = new ComboBox<>(
                FXCollections.observableArrayList(
                        "Trabajador",
                        "Invitado"
                )
        );
        cbTipo.setPromptText("Selecciona el tipo");
        cbTipo.setValue("Trabajador");

        ComboBox<Empresa> cbEmpresa = crearComboEmpresas();

        ComboBox<String> cbEstado = crearComboEstados();

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(12);
        formulario.setPadding(new Insets(20));

        formulario.add(new Label("Nombre:"), 0, 0);
        formulario.add(txtNombre, 1, 0);

        formulario.add(new Label("Documento:"), 0, 1);
        formulario.add(txtDocumento, 1, 1);

        formulario.add(new Label("Tipo:"), 0, 2);
        formulario.add(cbTipo, 1, 2);

        formulario.add(new Label("Empresa:"), 0, 3);
        formulario.add(cbEmpresa, 1, 3);

        formulario.add(new Label("Estado:"), 0, 4);
        formulario.add(cbEstado, 1, 4);

        dialog.getDialogPane().setContent(formulario);

        Button botonGuardar = (Button)
                dialog.getDialogPane().lookupButton(guardar);

        botonGuardar.addEventFilter(
                ActionEvent.ACTION,
                evento -> {

                    if (txtNombre.getText() == null
                            || txtNombre.getText().isBlank()
                            || txtDocumento.getText() == null
                            || txtDocumento.getText().isBlank()
                            || cbTipo.getValue() == null
                            || cbEstado.getValue() == null) {

                        mostrarAlerta(
                                Alert.AlertType.WARNING,
                                "Datos incompletos",
                                "Completa el nombre, documento, tipo y estado."
                        );

                        evento.consume();
                    }
                }
        );

        dialog.setResultConverter(boton -> {

            if (boton == guardar) {

                Empresa empresaSeleccionada =
                        cbEmpresa.getValue();

                Integer empresaId =
                        empresaSeleccionada == null
                                ? null
                                : empresaSeleccionada.getId();

                return new Persona(
                        0,
                        txtNombre.getText().trim(),
                        txtDocumento.getText().trim(),
                        empresaId,
                        cbTipo.getValue(),
                        obtenerIdEstado(cbEstado.getValue()),
                        null
                );
            }

            return null;
        });

        Optional<Persona> resultado = dialog.showAndWait();

        resultado.ifPresent(persona -> {

            try {
                personaService.crearPersona(persona, usuario);

                cargarPersonas();

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Registro exitoso",
                        "La persona se registró correctamente."
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
                        "No fue posible registrar la persona."
                );
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleEditarPersona() {

        Persona personaSeleccionada =
                tablaPersonas.getSelectionModel().getSelectedItem();

        if (personaSeleccionada == null) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Selección requerida",
                    "Selecciona una persona de la tabla."
            );
            return;
        }

        Dialog<Persona> dialog = new Dialog<>();

        dialog.setTitle("Editar persona");
        dialog.setHeaderText("Modificar información de la persona");

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
                personaSeleccionada.getNombre()
        );

        TextField txtDocumento = new TextField(
                personaSeleccionada.getDocumentoIdentidad()
        );

        ComboBox<String> cbTipo = new ComboBox<>(
                FXCollections.observableArrayList(
                        "Trabajador",
                        "Invitado"
                )
        );
        cbTipo.setValue(personaSeleccionada.getTipoPersona());

        ComboBox<Empresa> cbEmpresa = crearComboEmpresas();

        for (Empresa empresa : empresasDisponibles) {
            if (personaSeleccionada.getEmpresaId() != null
                    && empresa.getId()
                    == personaSeleccionada.getEmpresaId()) {
                cbEmpresa.setValue(empresa);
                break;
            }
        }

        ComboBox<String> cbEstado = crearComboEstados();
        cbEstado.setValue(
                obtenerNombreEstado(
                        personaSeleccionada.getEstadoAccesoId()
                )
        );

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(12);
        formulario.setPadding(new Insets(20));

        formulario.add(new Label("Nombre:"), 0, 0);
        formulario.add(txtNombre, 1, 0);

        formulario.add(new Label("Documento:"), 0, 1);
        formulario.add(txtDocumento, 1, 1);

        formulario.add(new Label("Tipo:"), 0, 2);
        formulario.add(cbTipo, 1, 2);

        formulario.add(new Label("Empresa:"), 0, 3);
        formulario.add(cbEmpresa, 1, 3);

        formulario.add(new Label("Estado:"), 0, 4);
        formulario.add(cbEstado, 1, 4);

        dialog.getDialogPane().setContent(formulario);

        Button botonGuardar = (Button)
                dialog.getDialogPane().lookupButton(guardar);

        botonGuardar.addEventFilter(
                ActionEvent.ACTION,
                evento -> {

                    if (txtNombre.getText() == null
                            || txtNombre.getText().isBlank()
                            || txtDocumento.getText() == null
                            || txtDocumento.getText().isBlank()
                            || cbTipo.getValue() == null
                            || cbEstado.getValue() == null) {

                        mostrarAlerta(
                                Alert.AlertType.WARNING,
                                "Datos incompletos",
                                "Completa el nombre, documento, tipo y estado."
                        );

                        evento.consume();
                    }
                }
        );

        dialog.setResultConverter(boton -> {

            if (boton == guardar) {

                Empresa empresaSeleccionada =
                        cbEmpresa.getValue();

                Integer empresaId =
                        empresaSeleccionada == null
                                ? null
                                : empresaSeleccionada.getId();

                return new Persona(
                        personaSeleccionada.getId(),
                        txtNombre.getText().trim(),
                        txtDocumento.getText().trim(),
                        empresaId,
                        cbTipo.getValue(),
                        obtenerIdEstado(cbEstado.getValue()),
                        personaSeleccionada.getUrlFoto()
                );
            }

            return null;
        });

        Optional<Persona> resultado = dialog.showAndWait();

        resultado.ifPresent(persona -> {

            try {
                boolean actualizado =
                        personaService.actualizarPersona(
                                persona,
                                usuario
                        );

                if (actualizado) {
                    cargarPersonas();

                    mostrarAlerta(
                            Alert.AlertType.INFORMATION,
                            "Actualización exitosa",
                            "La persona se actualizó correctamente."
                    );

                } else {
                    mostrarAlerta(
                            Alert.AlertType.WARNING,
                            "Sin cambios",
                            "No se encontró la persona para actualizar."
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
                        "No fue posible actualizar la persona."
                );
                e.printStackTrace();
            }
        });
    }

    private ComboBox<Empresa> crearComboEmpresas() {

        ComboBox<Empresa> combo = new ComboBox<>(
                FXCollections.observableArrayList(
                        empresasDisponibles
                )
        );

        combo.setPromptText("Sin empresa");

        combo.setConverter(new StringConverter<>() {

            @Override
            public String toString(Empresa empresa) {
                return empresa == null
                        ? ""
                        : empresa.getNombre();
            }

            @Override
            public Empresa fromString(String texto) {
                return null;
            }
        });

        return combo;
    }

    private ComboBox<String> crearComboEstados() {

        ComboBox<String> combo = new ComboBox<>(
                FXCollections.observableArrayList(
                        "Activo",
                        "Con Prohibición de Ingreso"
                )
        );

        combo.setPromptText("Selecciona el estado");
        combo.setValue("Activo");

        return combo;
    }

    private int obtenerIdEstado(String estado) {

        if ("Activo".equals(estado)) {
            return 1;
        }

        if ("Con Prohibición de Ingreso".equals(estado)) {
            return 2;
        }

        return 0;
    }

    private String obtenerNombreEstado(int estadoId) {

        if (estadoId == 1) {
            return "Activo";
        }

        if (estadoId == 2) {
            return "Con Prohibición de Ingreso";
        }

        return "Desconocido";
    }

    private String obtenerNombreEmpresa(Integer empresaId) {

        if (empresaId == null) {
            return "Sin empresa";
        }

        return nombresEmpresas.getOrDefault(
                empresaId,
                "No disponible"
        );
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