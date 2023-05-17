package es.serversurvival.v2.pixelcoins.empresas._shared.empleados;

import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.Empresa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmpleadosRepository {
    void save(Empleado empleado);

    Optional<Empleado> findById(UUID empleadoId);

    Optional<Empleado> findByEmpresaIdYEmpleadoJugadorId(UUID empresaId, UUID empleadoJugadorId);

    List<Empleado> findByJugadorId(UUID jugadorId);

    List<Empleado> findByEmpresaId(UUID empresaId);

    List<Empresa> findAll();
}
