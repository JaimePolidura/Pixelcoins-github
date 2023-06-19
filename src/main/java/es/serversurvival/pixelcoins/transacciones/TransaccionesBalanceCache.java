package es.serversurvival.pixelcoins.transacciones;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@AllArgsConstructor
public final class TransaccionesBalanceCache {
    private final Map<UUID, AtomicDouble> balanceByEntidadId;

    public TransaccionesBalanceCache() {
        this.balanceByEntidadId = new ConcurrentHashMap<>();
    }

    public double get(UUID entidadId,
                      Function<UUID, Double> pagadoPixelcoinsDb,
                      Function<UUID, Double> pagadorPixelcoinsDb) {
        if(!balanceByEntidadId.containsKey(entidadId)){
            balanceByEntidadId.putIfAbsent(entidadId, new AtomicDouble(0));
            return balanceByEntidadId.get(entidadId)
                    .addAndGet(pagadoPixelcoinsDb.apply(entidadId) - pagadorPixelcoinsDb.apply(entidadId));
        }

        return balanceByEntidadId.get(entidadId).get();
    }

    public void update(Transaccion transaccion) {
        balanceByEntidadId.putIfAbsent(transaccion.getPagadoId(), new AtomicDouble(0));
        AtomicDouble balancePagado = balanceByEntidadId.get(transaccion.getPagadoId());
        balancePagado.addAndGet(transaccion.getPixelcoins());

        balanceByEntidadId.putIfAbsent(transaccion.getPagadorId(), new AtomicDouble(0));
        AtomicDouble balancePagador = balanceByEntidadId.get(transaccion.getPagadorId());
        do {
        }while (!balancePagador.compareAndSet(balancePagador.get(), balancePagador.get() - transaccion.getPixelcoins()));
    }
}
