package es.serversurvival.pixelcoins.deudas.pagarcuotas;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.RetoProgresivoService;
import es.serversurvival.pixelcoins.transacciones.application.MovimientosService;
import es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class CuotaPagadaRetoProgresivoService implements RetoProgresivoService {
    private final MovimientosService movimientosService;

    @Override
    public double getCantidad(UUID jugadorId, Object otro) {
        return movimientosService.getBalance(TipoTransaccion.DEUDAS_CUOTA, jugadorId);
    }
}
