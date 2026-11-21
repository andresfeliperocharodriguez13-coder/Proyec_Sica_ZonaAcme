
package com.sica.acme;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        Label titulo = new Label("SICA - Zona Acme");
        Label mensaje = new Label("Sistema Integrado de Control de Acceso");

        Button boton = new Button("Comenzar");

        VBox contenedor = new VBox(15);
        contenedor.getChildren().addAll(titulo, mensaje, boton);

        Scene escena = new Scene(contenedor, 800, 500);

        stage.setTitle("SICA - Zona Acme");
        stage.setScene(escena);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
