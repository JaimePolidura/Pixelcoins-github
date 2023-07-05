package es.serversurvival.pixelcoins.deudas.prestar;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasService;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.deudas._shared.domain.EstadoDeuda;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.RetoProgresivoService;
import es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.application.TransaccionesBalanceService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class DeudasPrestarAcredorRetoProgresivoService implements RetoProgresivoService {
    private final TransaccionesBalanceService transaccionesBalanceService;
    private final DeudasService deudasService;

    @Override
    public double getCantidad(UUID jugadorId, Object otro) {
        double totalPixelcoinsPrestadas = -1 * transaccionesBalanceService.get(TipoTransaccion.DEUDAS_PRIMER_DESEMBOLSO, jugadorId);
        double totalPixelcoinsDeudasCanceladas = deudasService.findByAcredorJugadorId(jugadorId).stream()
                .filter(deuda -> deuda.getEstadoDeuda() == EstadoDeuda.CANCELADA)
                .mapToDouble(Deuda::getPixelcoinsRestantesDePagar)
                .sum();

        return totalPixelcoinsPrestadas - totalPixelcoinsDeudasCanceladas;
    }
}
