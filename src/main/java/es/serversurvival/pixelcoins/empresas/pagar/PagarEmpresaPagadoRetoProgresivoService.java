package es.serversurvival.pixelcoins.empresas.pagar;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.retos._shared.RetoProgresivoService;
import es.serversurvival.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.TransaccionesBalanceService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class PagarEmpresaPagadoRetoProgresivoService implements RetoProgresivoService {
    private final TransaccionesBalanceService transaccionesBalanceService;

    @Override
    public double getCantidad(UUID jugadorId, Object otro) {
        UUID empresaId = (UUID) otro;

        return transaccionesBalanceService.get(TipoTransaccion.EMPRESAS_PAGAR, empresaId, jugadorId);
    }
}
