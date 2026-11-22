package com.sica.acme.infrastructure.database;

import org.mindrot.jbcrypt.BCrypt;

public class GenerarPassword {
    public static void main(String[] args) {
        String contrasena = "Sica2026*";
        String hash = BCrypt.hashpw(
                contrasena,
                BCrypt.gensalt(12)
        );
        System.out.println("Hash generado:");
        System.out.println(hash);
    }
}
