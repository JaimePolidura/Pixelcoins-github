package es.serversurvival.pixelcoins.transacciones.application;

import com.google.common.util.concurrent.AtomicDouble;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.transacciones.domain.Movimiento;
import es.serversurvival.pixelcoins.transacciones.domain.Transaccion;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class TransaccionesBalanceCache {
    private final Map<UUID, AtomicDouble> balanceByEntidadId;

    public TransaccionesBalanceCache() {
        this.balanceByEntidadId = new ConcurrentHashMap<>();
    }

    public double get(UUID entidadId, Function<UUID, Double> callbackPixelcoinsBalance){
        if(!balanceByEntidadId.containsKey(entidadId)){
            double balance = callbackPixelcoinsBalance.apply(entidadId);
            balanceByEntidadId.putIfAbsent(entidadId, new AtomicDouble(balance));
        }

        return balanceByEntidadId.get(entidadId).get();
    }

    public void update(Movimiento movimiento, Function<UUID, Double> callbackPixelcoinsBalance) {
        balanceByEntidadId.putIfAbsent(movimiento.getEntidadId(), new AtomicDouble(callbackPixelcoinsBalance.apply(movimiento.getEntidadId())));
        AtomicDouble balance = balanceByEntidadId.get(movimiento.getEntidadId());
        balance.addAndGet(movimiento.getPixelcoins());
    }
}
