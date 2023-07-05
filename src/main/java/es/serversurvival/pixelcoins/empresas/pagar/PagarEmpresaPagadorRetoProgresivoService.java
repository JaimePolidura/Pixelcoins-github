package es.serversurvival.pixelcoins.empresas.pagar;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.RetoProgresivoService;
import es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.application.TransaccionesBalanceService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class PagarEmpresaPagadorRetoProgresivoService implements RetoProgresivoService {
    private final TransaccionesBalanceService transaccionesBalanceService;

    @Override
    public double getCantidad(UUID jugadorId, Object otro) {
        return -1 * transaccionesBalanceService.get(TipoTransaccion.EMPRESAS_PAGAR, jugadorId);
    }
}
