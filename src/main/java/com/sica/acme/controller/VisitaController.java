package com.sica.acme.controller;

import com.sica.acme.model.Persona;
import com.sica.acme.model.Usuario;
import com.sica.acme.model.Visita;

import com.sica.acme.repository.PersonaRepository;
import com.sica.acme.repository.PermisoRepository;
import com.sica.acme.repository.VisitaRepository;

import com.sica.acme.service.PersonaService;
import com.sica.acme.service.PermisoService;
import com.sica.acme.service.VisitaService;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import javafx.fxml.FXML;

import javafx.beans.property.ReadOnlyStringWrapper;

import javafx.util.StringConverter;

import java.sql.SQLException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitaController {

    @FXML private TableView<Visita> tablaVisitas;

    @FXML private TableColumn<Visita, String> colId;
    @FXML private TableColumn<Visita, String> colPersona;
    @FXML private TableColumn<Visita, String> colAnfitrion;
    @FXML private TableColumn<Visita, String> colFecha;
    @FXML private TableColumn<Visita, String> colEstado;
    @FXML private TableColumn<Visita, String> colPlaca;

    @FXML private Button btnNuevaVisita;
    @FXML private Button btnAprobar;
    @FXML private Button btnRechazar;
    @FXML private Button btnEntrada;
    @FXML private Button btnSalida;
    @FXML private Button btnActualizar;

    private Usuario usuario;

    private final PermisoService permisoService =
            new PermisoService(new PermisoRepository());

    private final VisitaService visitaService =
            new VisitaService(
                    new VisitaRepository(),
                    permisoService
            );

    private final PersonaService personaService =
            new PersonaService(
                    new PersonaRepository(),
                    permisoService
            );

    private final Map<Integer, String> nombresPersonas =
            new HashMap<>();

    @FXML
    private void initialize() {

        configurarTabla();
    }

    public void setUsuario(Usuario usuario) {

        this.usuario = usuario;

        actualizarPermisos();
        cargarPersonas();
        cargarVisitas();
    }

    // Configurar las columnas de la tabla
    private void configurarTabla() {

        colId.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(
                        String.valueOf(data.getValue().getId())
                )
        );

        colPersona.setCellValueFactory(data -> {

            int personaId = data.getValue().getPersonaId();

            return new ReadOnlyStringWrapper(
                    nombresPersonas.getOrDefault(
                            personaId,
                            "ID: " + personaId
                    )
            );
        });

        colAnfitrion.setCellValueFactory(data -> {

            Integer anfitrionId =
                    data.getValue().getAnfitrionId();

            if (anfitrionId == null) {
                return new ReadOnlyStringWrapper("No asignado");
            }

            return new ReadOnlyStringWrapper(
                    nombresPersonas.getOrDefault(
                            anfitrionId,
                            "ID: " + anfitrionId
                    )
            );
        });

        colFecha.setCellValueFactory(data -> {

            LocalDateTime fecha =
                    data.getValue().getFechaProgramada();

            String resultado = fecha == null
                    ? ""
                    : fecha.format(
                            DateTimeFormatter.ofPattern(
                                    "dd/MM/yyyy HH:mm"
                            )
                    );

            return new ReadOnlyStringWrapper(resultado);
        });

        colEstado.setCellValueFactory(data -> {

            return new ReadOnlyStringWrapper(
                    obtenerNombreEstado(
                            data.getValue().getEstadoVisitaId()
                    )
            );
        });

        colPlaca.setCellValueFactory(data -> {

            String placa = data.getValue().getVehiculoPlaca();

            return new ReadOnlyStringWrapper(
                    placa == null || placa.isBlank()
                            ? "Sin vehículo"
                            : placa
            );
        });
    }

    // Cargar permisos del usuario
    private void actualizarPermisos() {

        if (usuario == null) {
            return;
        }

        try {

            btnNuevaVisita.setDisable(
                    !permisoService.tienePermiso(
                            usuario,
                            "registrar_visita"
                    )
            );

            btnAprobar.setDisable(
                    !permisoService.tienePermiso(
                            usuario,
                            "aprobar_visita"
                    )
            );

            btnRechazar.setDisable(
                    !permisoService.tienePermiso(
                            usuario,
                            "rechazar_visita"
                    )
            );

            btnEntrada.setDisable(
                    !permisoService.tienePermiso(
                            usuario,
                            "registrar_entrada"
                    )
            );

            btnSalida.setDisable(
                    !permisoService.tienePermiso(
                            usuario,
                            "registrar_salida"
                    )
            );

            btnActualizar.setDisable(
                    !permisoService.tienePermiso(
                            usuario,
                            "consultar_visitas"
                    )
            );

        } catch (SQLException e) {

            mostrarAlerta(
                    "Error de permisos",
                    "No fue posible consultar los permisos del usuario."
            );
        }
    }

    // Cargar personas para mostrar sus nombres
    private void cargarPersonas() {

        nombresPersonas.clear();

        try {

            List<Persona> personas =
                    personaService.listarPersonas(usuario);

            for (Persona persona : personas) {

                nombresPersonas.put(
                        persona.getId(),
                        persona.getNombre()
                );
            }

        } catch (Exception e) {

            mostrarAlerta(
                    "Error",
                    "No fue posible cargar las personas: "
                            + e.getMessage()
            );
        }
    }

    // Consultar visitas registradas
    private void cargarVisitas() {

        if (usuario == null) {
            return;
        }

        try {

            List<Visita> visitas =
                    visitaService.listarVisitas(usuario);

            tablaVisitas.setItems(
                    FXCollections.observableArrayList(visitas)
            );

        } catch (Exception e) {

            mostrarAlerta(
                    "Error al cargar visitas",
                    e.getMessage()
            );
        }
    }

    // Registrar una nueva visita
    @FXML
    private void handleNuevaVisita() {

        try {

            List<Persona> personas =
                    personaService.listarPersonas(usuario);

            if (personas.isEmpty()) {

                mostrarAlerta(
                        "Sin personas",
                        "Primero debes registrar una persona."
                );

                return;
            }

            Dialog<Visita> dialog = new Dialog<>();

            dialog.setTitle("Registrar visita");
            dialog.setHeaderText(
                    "Completa la información de la visita."
            );

            ButtonType botonGuardar =
                    new ButtonType(
                            "Guardar",
                            ButtonBar.ButtonData.OK_DONE
                    );

            dialog.getDialogPane().getButtonTypes().addAll(
                    botonGuardar,
                    ButtonType.CANCEL
            );

            ComboBox<Persona> cmbPersona =
                    new ComboBox<>(
                            FXCollections.observableArrayList(personas)
                    );

            ComboBox<Persona> cmbAnfitrion =
                    new ComboBox<>(
                            FXCollections.observableArrayList(personas)
                    );

            configurarComboPersonas(cmbPersona);
            configurarComboPersonas(cmbAnfitrion);

            cmbPersona.setPromptText("Seleccionar persona");
            cmbAnfitrion.setPromptText("Opcional");

            DatePicker fecha = new DatePicker(LocalDate.now());

            TextField hora = new TextField("08:00");
            hora.setPromptText("HH:mm");

            TextField placa = new TextField();
            placa.setPromptText("Opcional");

            GridPane formulario = new GridPane();

            formulario.setHgap(10);
            formulario.setVgap(12);

            formulario.addRow(
                    0,
                    new Label("Persona:"),
                    cmbPersona
            );

            formulario.addRow(
                    1,
                    new Label("Anfitrión:"),
                    cmbAnfitrion
            );

            formulario.addRow(
                    2,
                    new Label("Fecha:"),
                    fecha
            );

            formulario.addRow(
                    3,
                    new Label("Hora:"),
                    hora
            );

            formulario.addRow(
                    4,
                    new Label("Placa:"),
                    placa
            );

            dialog.getDialogPane().setContent(formulario);

            dialog.setResultConverter(boton -> {

                if (boton == botonGuardar) {

                    if (cmbPersona.getValue() == null
                            || fecha.getValue() == null
                            || hora.getText().isBlank()) {

                        return null;
                    }

                    LocalTime horaSeleccionada =
                            LocalTime.parse(hora.getText().trim());

                    Visita visita = new Visita();

                    visita.setPersonaId(
                            cmbPersona.getValue().getId()
                    );

                    if (cmbAnfitrion.getValue() != null) {

                        visita.setAnfitrionId(
                                cmbAnfitrion.getValue().getId()
                        );

                    } else {

                        visita.setAnfitrionId(null);
                    }

                    visita.setFechaProgramada(
                            LocalDateTime.of(
                                    fecha.getValue(),
                                    horaSeleccionada
                            )
                    );

                    visita.setVehiculoPlaca(
                            placa.getText().isBlank()
                                    ? null
                                    : placa.getText().trim()
                    );

                    return visita;
                }

                return null;
            });

            dialog.showAndWait().ifPresent(visita -> {

                try {

                    visitaService.registrarVisita(
                            visita,
                            usuario
                    );

                    cargarVisitas();

                    mostrarMensaje(
                            "Visita registrada",
                            "La visita fue registrada correctamente."
                    );

                } catch (Exception e) {

                    mostrarAlerta(
                            "Error al registrar",
                            e.getMessage()
                    );
                }
            });

        } catch (Exception e) {

            mostrarAlerta(
                    "Error",
                    e.getMessage()
            );
        }
    }

    // Configurar cómo aparecen las personas en los ComboBox
    private void configurarComboPersonas(
            ComboBox<Persona> combo
    ) {

        combo.setConverter(new StringConverter<>() {

            @Override
            public String toString(Persona persona) {

                if (persona == null) {
                    return "";
                }

                return persona.getNombre()
                        + " - "
                        + persona.getDocumentoIdentidad();
            }

            @Override
            public Persona fromString(String texto) {
                return null;
            }
        });
    }

    // Aprobar visita seleccionada
    @FXML
    private void handleAprobarVisita() {

        ejecutarAccion(
                "La visita fue aprobada correctamente.",
                id -> visitaService.aprobarVisita(id, usuario)
        );
    }

    // Rechazar visita seleccionada
    @FXML
    private void handleRechazarVisita() {

        ejecutarAccion(
                "La visita fue rechazada.",
                id -> visitaService.rechazarVisita(id, usuario)
        );
    }

    // Registrar entrada
    @FXML
    private void handleRegistrarEntrada() {

        ejecutarAccion(
                "La entrada fue registrada correctamente.",
                id -> visitaService.registrarEntrada(id, usuario)
        );
    }

    // Registrar salida
    @FXML
    private void handleRegistrarSalida() {

        ejecutarAccion(
                "La salida fue registrada correctamente.",
                id -> visitaService.registrarSalida(id, usuario)
        );
    }

    // Ejecutar acciones sobre la visita seleccionada
    private void ejecutarAccion(
            String mensaje,
            OperacionVisita operacion
    ) {

        Visita seleccionada =
                tablaVisitas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {

            mostrarAlerta(
                    "Selecciona una visita",
                    "Debes seleccionar una visita de la tabla."
            );

            return;
        }

        try {

            operacion.ejecutar(seleccionada.getId());

            cargarVisitas();

            mostrarMensaje(
                    "Operación realizada",
                    mensaje
            );

        } catch (Exception e) {

            mostrarAlerta(
                    "No se pudo realizar la operación",
                    e.getMessage()
            );
        }
    }

    @FunctionalInterface
    private interface OperacionVisita {

        void ejecutar(int id) throws SQLException;
    }

    // Actualizar la tabla
    @FXML
    private void handleActualizar() {

        cargarPersonas();
        cargarVisitas();
    }

    // Obtener nombre del estado
    private String obtenerNombreEstado(int estadoId) {

        return switch (estadoId) {

            case 1 -> "Dentro";
            case 2 -> "Fuera";
            case 3 -> "Pendiente de aprobación";
            case 4 -> "Aprobado";
            case 5 -> "Rechazado";
            case 6 -> "Expirado";
            case 7 -> "Cerrada por sistema";
            case 8 -> "Pendiente por olvido";
            default -> "Desconocido";
        };
    }

    // Mostrar alerta de error
    private void mostrarAlerta(
            String titulo,
            String mensaje
    ) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(
                mensaje == null ? "Ha ocurrido un error." : mensaje
        );

        alerta.showAndWait();
    }

    // Mostrar mensaje de éxito
    private void mostrarMensaje(
            String titulo,
            String mensaje
    ) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }
}
