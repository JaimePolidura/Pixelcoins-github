package es.serversurvival.pixelcoins.empresas._shared.empleados;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmpleadosRepository {
    void save(Empleado empleado);

    Optional<Empleado> findById(UUID empleadoId);

    List<Empleado> findByEmpleadoJugadorId(UUID empleadoJugadorId);

    List<Empleado> findByEmpresaIdAndEmpleadoJugadorId(UUID empresaId, UUID empleadoJugadorId);

    List<Empleado> findByEmpresaId(UUID empresaId);

    void deleteById(UUID empleadoId);
}
