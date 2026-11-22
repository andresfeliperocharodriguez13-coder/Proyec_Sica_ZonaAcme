USE sica_acme;

CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE permisos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_permiso VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE rol_permisos (
    rol_id INT NOT NULL,
    permiso_id INT NOT NULL,
    PRIMARY KEY (rol_id, permiso_id),
    FOREIGN KEY (rol_id) REFERENCES roles(id),
    FOREIGN KEY (permiso_id) REFERENCES permisos(id)
);

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol_id INT NOT NULL,
    esta_activo BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

CREATE TABLE persona_estados_acceso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_estado VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE visita_estados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_estado VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE empresas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contacto_principal VARCHAR(100)
);

CREATE TABLE personas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    documento_identidad VARCHAR(20) NOT NULL UNIQUE,
    empresa_id INT,
    tipo_persona ENUM('Trabajador', 'Invitado') NOT NULL,
    estado_acceso_id INT NOT NULL,
    url_foto VARCHAR(255),
    FOREIGN KEY (empresa_id) REFERENCES empresas(id),
    FOREIGN KEY (estado_acceso_id) REFERENCES persona_estados_acceso(id)
);

CREATE TABLE visitas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    persona_id INT NOT NULL,
    fecha_entrada DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_salida DATETIME,
    estado_visita_id INT NOT NULL,
    vehiculo_placa VARCHAR(10),
    visita_aprobada_por INT,
    FOREIGN KEY (persona_id) REFERENCES personas(id),
    FOREIGN KEY (estado_visita_id) REFERENCES visita_estados(id),
    FOREIGN KEY (visita_aprobada_por) REFERENCES usuarios(id)
);

CREATE TABLE incidentes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    visita_id INT,
    reportado_por_id INT,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    descripcion TEXT NOT NULL,
    FOREIGN KEY (visita_id) REFERENCES visitas(id),
    FOREIGN KEY (reportado_por_id) REFERENCES usuarios(id)
);

CREATE TABLE bitacora_auditoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    fecha_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    accion_realizada VARCHAR(255) NOT NULL,
    tabla_afectada VARCHAR(100),
    registro_id_afectado INT,
    detalles TEXT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
