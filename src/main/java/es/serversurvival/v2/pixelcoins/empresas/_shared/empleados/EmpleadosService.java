package es.serversurvival.v2.pixelcoins.empresas._shared.empleados;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class EmpleadosService {
    private final EmpleadosRepository repository;

    public void save(Empleado empleado) {
        this.repository.save(empleado);
    }

    public Empleado getById(UUID empleadoId) {
        return this.repository.findById(empleadoId).orElseThrow(() -> new ResourceNotFound("Empleado no encontrado"));
    }

    public Empleado getEmpleoActivoByEmpresaIdAndEmpleadoJugadorId(UUID empresaId, UUID empleadoJugadorId) {
        return findEmpleoActivoByEmpresaIdAndEmpleadoJugadorId(empresaId, empleadoJugadorId)
                .orElseThrow(() -> new ResourceNotFound("Empleado no encontrado"));
    }

    public Optional<Empleado> findEmpleoActivoByEmpresaIdAndEmpleadoJugadorId(UUID empresaId, UUID empleadoJugadorId) {
        return this.repository.findByEmpresaIdAndEmpleadoJugadorId(empresaId, empleadoJugadorId).stream()
                .filter(Empleado::isEstaContratado)
                .findFirst();
    }

    public List<Empleado> findByEmpleadoJugadorId(UUID empleadoJugadorId) {
        return repository.findByEmpleadoJugadorId(empleadoJugadorId);
    }

    public List<Empleado> findEmpleoActivoByEmpresaId(UUID empresaId) {
        return this.repository.findByEmpresaId(empresaId).stream()
                .filter(Empleado::isEstaContratado)
                .toList();
    }

    public void deleteById(UUID empleadoId) {
        this.repository.deleteById(empleadoId);
    }
}
