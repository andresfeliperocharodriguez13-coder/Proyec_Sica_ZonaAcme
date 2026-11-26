
USE sica_acme;

-- =========================================
-- 1. RELACIONAR USUARIOS CON EMPRESAS
-- =========================================

ALTER TABLE usuarios
ADD COLUMN empresa_id INT NULL AFTER rol_id;

ALTER TABLE usuarios
ADD CONSTRAINT fk_usuarios_empresas
FOREIGN KEY (empresa_id) REFERENCES empresas(id);

-- =========================================
-- 2. ACTUALIZAR ESTRUCTURA DE VISITAS
-- =========================================

ALTER TABLE visitas
MODIFY COLUMN fecha_entrada DATETIME NULL DEFAULT NULL;

ALTER TABLE visitas
ADD COLUMN anfitrion_id INT NULL AFTER persona_id,
ADD COLUMN fecha_programada DATETIME NOT NULL AFTER anfitrion_id;

ALTER TABLE visitas
ADD CONSTRAINT fk_visitas_anfitrion
FOREIGN KEY (anfitrion_id) REFERENCES personas(id);

-- =========================================
-- 3. AGREGAR PERMISO DE CONSULTA DE EMPRESAS
-- =========================================

INSERT IGNORE INTO permisos (nombre_permiso, descripcion)
VALUES (
    'consultar_empresas',
    'Consultar información de empresas'
);

-- =========================================
-- 4. ASIGNAR PERMISOS
-- =========================================

INSERT IGNORE INTO rol_permisos (rol_id, permiso_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permisos p
WHERE r.nombre_rol IN (
    'Superusuario',
    'Supervisor de Seguridad'
)
AND p.nombre_permiso = 'consultar_empresas';
