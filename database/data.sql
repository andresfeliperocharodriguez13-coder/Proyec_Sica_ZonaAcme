
USE sica_acme;

-- =========================================
-- 1. ROLES DEL SISTEMA
-- =========================================

INSERT IGNORE INTO roles (nombre_rol) VALUES
('Superusuario'),
('Supervisor de Seguridad'),
('Guarda de Seguridad'),
('Funcionario de Empresa');


-- =========================================
-- 2. PERMISOS DEL SISTEMA
-- =========================================

INSERT IGNORE INTO permisos (nombre_permiso, descripcion) VALUES
('crear_usuario', 'Crear usuarios del sistema'),
('editar_usuario', 'Modificar usuarios del sistema'),
('eliminar_usuario', 'Eliminar usuarios del sistema'),
('gestionar_roles_permisos', 'Administrar roles y permisos'),

('crear_empresa', 'Registrar empresas'),
('editar_empresa', 'Modificar empresas'),
('eliminar_empresa', 'Eliminar empresas'),

('crear_persona', 'Registrar personas'),
('editar_persona', 'Modificar personas'),
('eliminar_persona', 'Eliminar personas'),
('consultar_personas', 'Consultar información de personas'),
('bloquear_persona', 'Bloquear el acceso de una persona'),
('desbloquear_persona', 'Restablecer el acceso de una persona'),

('registrar_visita', 'Registrar visitas'),
('consultar_visitas', 'Consultar visitas'),
('aprobar_visita', 'Aprobar visitas'),
('rechazar_visita', 'Rechazar visitas'),
('registrar_entrada', 'Registrar entrada de personas'),
('registrar_salida', 'Registrar salida de personas'),

('registrar_incidente', 'Registrar incidentes'),
('consultar_incidentes', 'Consultar incidentes'),

('consultar_reportes', 'Consultar reportes del sistema'),
('generar_reporte_auditoria', 'Generar reportes de auditoría');


-- =========================================
-- 3. PERMISOS DEL SUPERUSUARIO
-- =========================================

INSERT IGNORE INTO rol_permisos (rol_id, permiso_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permisos p
WHERE r.nombre_rol = 'Superusuario';


-- =========================================
-- 4. PERMISOS DEL SUPERVISOR
-- =========================================

INSERT IGNORE INTO rol_permisos (rol_id, permiso_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permisos p
WHERE r.nombre_rol = 'Supervisor de Seguridad'
AND p.nombre_permiso IN (
    'crear_usuario',
    'editar_usuario',
    'crear_empresa',
    'editar_empresa',
    'crear_persona',
    'editar_persona',
    'eliminar_persona',
    'consultar_personas',
    'bloquear_persona',
    'desbloquear_persona',
    'registrar_visita',
    'consultar_visitas',
    'aprobar_visita',
    'rechazar_visita',
    'registrar_entrada',
    'registrar_salida',
    'registrar_incidente',
    'consultar_incidentes',
    'consultar_reportes',
    'generar_reporte_auditoria'
);


-- =========================================
-- 5. PERMISOS DEL GUARDA DE SEGURIDAD
-- =========================================

INSERT IGNORE INTO rol_permisos (rol_id, permiso_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permisos p
WHERE r.nombre_rol = 'Guarda de Seguridad'
AND p.nombre_permiso IN (
    'consultar_personas',
    'registrar_visita',
    'consultar_visitas',
    'registrar_entrada',
    'registrar_salida',
    'registrar_incidente',
    'consultar_incidentes'
);


-- =========================================
-- 6. PERMISOS DEL FUNCIONARIO DE EMPRESA
-- =========================================

INSERT IGNORE INTO rol_permisos (rol_id, permiso_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permisos p
WHERE r.nombre_rol = 'Funcionario de Empresa'
AND p.nombre_permiso IN (
    'crear_persona',
    'editar_persona',
    'consultar_personas',
    'registrar_visita',
    'consultar_visitas',
    'aprobar_visita',
    'rechazar_visita'
);


-- =========================================
-- 7. ESTADOS DE ACCESO DE LAS PERSONAS
-- =========================================

INSERT IGNORE INTO persona_estados_acceso (nombre_estado) VALUES
('Activo'),
('Con Prohibición de Ingreso');


-- =========================================
-- 8. ESTADOS DE LAS VISITAS
-- =========================================

INSERT IGNORE INTO visita_estados (nombre_estado) VALUES
('Dentro'),
('Fuera'),
('Pendiente de Aprobación'),
('Aprobado'),
('Rechazado'),
('Expirado'),
('Cerrada por Sistema (Salida Olvidada)'),
('Pendiente de Aprobación por Olvido de Identificación');
