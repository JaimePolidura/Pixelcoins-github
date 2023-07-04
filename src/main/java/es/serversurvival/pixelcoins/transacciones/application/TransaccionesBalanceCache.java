package es.serversurvival.pixelcoins.transacciones.application;

import com.google.common.util.concurrent.AtomicDouble;
import es.serversurvival._shared.utils.Funciones;
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
            balanceByEntidadId.putIfAbsent(entidadId, new AtomicDouble(0));
            balanceByEntidadId.get(entidadId).addAndGet(callbackPixelcoinsBalance.apply(entidadId));
        }

        return balanceByEntidadId.get(entidadId).get();
    }

    public void update(Transaccion transaccion) {
        if(!transaccion.getPagadoId().equals(Funciones.NULL_ID)){
            balanceByEntidadId.putIfAbsent(transaccion.getPagadoId(), new AtomicDouble(0));
            AtomicDouble balancePagado = balanceByEntidadId.get(transaccion.getPagadoId());
            balancePagado.addAndGet(transaccion.getPixelcoins());
        }

        if(!transaccion.getPagadorId().equals(Funciones.NULL_ID)){
            balanceByEntidadId.putIfAbsent(transaccion.getPagadorId(), new AtomicDouble(0));
            AtomicDouble balancePagador = balanceByEntidadId.get(transaccion.getPagadorId());
            do {
            }while (!balancePagador.compareAndSet(balancePagador.get(), balancePagador.get() - transaccion.getPixelcoins()));
        }
    }
}
