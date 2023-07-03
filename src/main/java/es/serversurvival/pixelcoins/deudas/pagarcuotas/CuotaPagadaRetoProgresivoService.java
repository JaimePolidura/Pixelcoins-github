package es.serversurvival.pixelcoins.deudas.pagarcuotas;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.retos._shared.RetoProgresivoService;
import es.serversurvival.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.TransaccionesBalanceService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class CuotaPagadaRetoProgresivoService implements RetoProgresivoService {
    private final TransaccionesBalanceService transaccionesBalanceService;

    @Override
    public double getCantidad(UUID jugadorId, Object otro) {
        return transaccionesBalanceService.get(TipoTransaccion.DEUDAS_CUOTA, jugadorId);
    }
}
