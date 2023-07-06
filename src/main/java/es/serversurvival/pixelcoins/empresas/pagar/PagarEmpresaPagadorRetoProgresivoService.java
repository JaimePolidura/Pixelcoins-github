package es.serversurvival.pixelcoins.empresas.pagar;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.RetoProgresivoService;
import es.serversurvival.pixelcoins.transacciones.application.MovimientosService;
import es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class PagarEmpresaPagadorRetoProgresivoService implements RetoProgresivoService {
    private final MovimientosService movimientosService;

    @Override
    public double getCantidad(UUID jugadorId, Object otro) {
        return -1 * movimientosService.getBalance(TipoTransaccion.EMPRESAS_PAGAR, jugadorId);
    }
}
