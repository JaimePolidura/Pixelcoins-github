package es.serversurvival.pixelcoins.transacciones.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins.transacciones.domain.*;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MovimientosService {
    private final TransaccionesBalanceCache transaccionesBalanceCache;
    private final MovimientoRepository repository;

    public void save(Movimiento movimiento) {
        repository.save(movimiento);
        transaccionesBalanceCache.update(
                movimiento,
                this::getBalance
        );
    }

    public double getBalance(UUID entidadId) {
        return transaccionesBalanceCache.get(entidadId, repository::getBalance);
    }

    public double getBalance(TipoTransaccion tipoTransaccion, UUID entidadId) {
        return repository.getBalance(tipoTransaccion, entidadId);
    }

    public double getBalance(TipoTransaccion tipoTransaccion, UUID entidadId, UUID otro) {
        return repository.getBalance(tipoTransaccion, entidadId, otro);
    }

    public List<Movimiento> findByEntidadIdOrderByFecha(UUID entidadId, int limit) {
        return repository.findByEntidadIdOrderByFecha(entidadId, limit);
    }
}
