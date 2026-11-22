package com.sica.acme.infrastructure.database;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionTest {

    public static void main(String[] args) {

        try (Connection connection = ConnectionFactory.getConnection()) {

            System.out.println("¡Conexión exitosa con MySQL!");
            System.out.println("Base de datos: "
                    + connection.getCatalog());

        } catch (SQLException e) {

            System.out.println("Error al conectar con MySQL.");
            e.printStackTrace();

        }
    }
}