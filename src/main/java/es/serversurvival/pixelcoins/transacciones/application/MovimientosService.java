package es.serversurvival.pixelcoins.transacciones.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.transacciones.domain.Movimiento;
import es.serversurvival.pixelcoins.transacciones.domain.MovimientoRepository;
import es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class MovimientosService {
    private final MovimientoRepository repository;

    public void save(Movimiento movimiento) {
        repository.save(movimiento);
    }

    public List<Movimiento> findByEntidadIdOrderByFecha(UUID entidadId, int limit) {
        return repository.findByEntidadIdOrderByFecha(entidadId, limit);
    }

    public double getBalance(TipoTransaccion tipoTransaccion, UUID entidadId) {
        return this.repository.getBalance(tipoTransaccion, entidadId);
    }

    public double getBalance(TipoTransaccion tipoTransaccion, UUID entidadId, UUID otro) {
        return this.repository.getBalance(tipoTransaccion, entidadId, otro);
    }
}
