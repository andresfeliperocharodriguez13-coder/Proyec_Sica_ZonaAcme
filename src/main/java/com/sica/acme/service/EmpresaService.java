
package com.sica.acme.service;

import com.sica.acme.model.Empresa;
import com.sica.acme.model.Usuario;
import com.sica.acme.repository.EmpresaRepositoryInterface;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EmpresaService {

    private final EmpresaRepositoryInterface empresaRepository;
    private final PermisoService permisoService;

    public EmpresaService(
            EmpresaRepositoryInterface empresaRepository,
            PermisoService permisoService) {

        this.empresaRepository = Objects.requireNonNull(empresaRepository);
        this.permisoService = Objects.requireNonNull(permisoService);
    }

    public void crearEmpresa(Empresa empresa, Usuario usuario)
            throws SQLException {

        verificarPermiso(usuario, "crear_empresa");

        if (empresa == null || empresa.getNombre() == null
                || empresa.getNombre().isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la empresa es obligatorio.");
        }

        empresa.setNombre(empresa.getNombre().trim());
        empresaRepository.crear(empresa);
    }

    public List<Empresa> listarEmpresas(Usuario usuario)
            throws SQLException {

        verificarPermiso(usuario, "consultar_empresas");

        return empresaRepository.listarTodos();
    }

    public Optional<Empresa> buscarPorId(int id, Usuario usuario)
            throws SQLException {

        verificarPermiso(usuario, "consultar_empresas");

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El identificador de la empresa no es válido.");
        }

        return empresaRepository.buscarPorId(id);
    }

    public boolean actualizarEmpresa(Empresa empresa, Usuario usuario)
            throws SQLException {

        verificarPermiso(usuario, "editar_empresa");

        if (empresa == null || empresa.getNombre() == null
                || empresa.getNombre().isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la empresa es obligatorio.");
        }

        if (empresa.getId() <= 0) {
            throw new IllegalArgumentException(
                    "El identificador de la empresa no es válido.");
        }

        empresa.setNombre(empresa.getNombre().trim());

        return empresaRepository.actualizar(empresa);
    }

    public boolean eliminarEmpresa(int id, Usuario usuario)
            throws SQLException {

        verificarPermiso(usuario, "eliminar_empresa");

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El identificador de la empresa no es válido.");
        }

        try {
            return empresaRepository.eliminar(id);

        } catch (SQLException e) {
            if (e.getSQLState() != null
                    && e.getSQLState().startsWith("23")) {
                throw new IllegalStateException(
                        "No se puede eliminar esta empresa porque tiene "
                                + "personas o usuarios relacionados.",
                        e
                );
            }

            throw e;
        }
    }

    private void verificarPermiso(
            Usuario usuario, String nombrePermiso)
            throws SQLException {

        if (!permisoService.tienePermiso(usuario, nombrePermiso)) {
            throw new SecurityException(
                    "No tienes permiso para realizar esta operación.");
        }
    }
}
