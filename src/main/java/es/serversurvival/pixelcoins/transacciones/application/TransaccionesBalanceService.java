package es.serversurvival.pixelcoins.transacciones.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.transacciones.application.TransaccionesBalanceCache;
import es.serversurvival.pixelcoins.transacciones.domain.MovimientoRepository;
import es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public class TransaccionesBalanceService {
    private final TransaccionesBalanceCache transaccionesBalanceCache;
    private final MovimientoRepository movimientoRepository;

    public double get(UUID entidadId) {
        return transaccionesBalanceCache.get(entidadId, movimientoRepository::getBalance);
    }

    public double get(TipoTransaccion tipoTransaccion, UUID entidadId) {
        return movimientoRepository.getBalance(tipoTransaccion, entidadId);
    }

    public double get(TipoTransaccion tipoTransaccion, UUID entidadId, UUID otro) {
        return movimientoRepository.getBalance(tipoTransaccion, entidadId, otro);
    }
}
