package es.serversurvival.pixelcoins.empresas.pagar;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.RetoProgresivoService;
import es.serversurvival.pixelcoins.transacciones.application.MovimientosService;
import es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class PagarEmpresaPagadoRetoProgresivoService implements RetoProgresivoService {
    private final MovimientosService movimeintosService;

    @Override
    public double getCantidad(UUID jugadorId, Object otro) {
        UUID empresaId = (UUID) otro;

        return movimeintosService.getBalance(TipoTransaccion.EMPRESAS_PAGAR, empresaId, jugadorId);
    }
}
