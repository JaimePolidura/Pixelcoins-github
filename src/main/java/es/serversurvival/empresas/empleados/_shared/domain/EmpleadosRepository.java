package es.serversurvival.empresas.empleados._shared.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmpleadosRepository {
    void save(Empleado empleado);

    Optional<Empleado> findById(UUID empleadoId);

    List<Empleado> findByJugador(String jugador);

    List<Empleado> findByEmpresa(String empresa);

    List<Empleado> findAll();

    void deleteById(UUID id);
}
