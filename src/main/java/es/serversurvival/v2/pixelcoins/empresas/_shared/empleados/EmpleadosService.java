package es.serversurvival.v2.pixelcoins.empresas._shared.empleados;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.Empresa;
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

    public Optional<Empleado> findByEmpresaIdYEmpleadoJugadorId(UUID empresaId, UUID empleadoJugadorId) {
        return this.repository.findByEmpresaIdYEmpleadoJugadorId(empresaId, empleadoJugadorId);
    }

    public List<Empleado> findByJugadorId(UUID jugadorId) {
        return this.repository.findByJugadorId(jugadorId);
    }

    public List<Empleado> findByEmpresaId(UUID empresaId) {
        return this.repository.findByEmpresaId(empresaId);
    }

    public List<Empresa> findAll() {
        return this.repository.findAll();
    }
}
