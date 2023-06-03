package es.serversurvival.v2.pixelcoins.deudas._shared;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v2.pixelcoins.jugadores.patrimonio.CalculadorPatrimonio;
import es.serversurvival.v2.pixelcoins.jugadores.patrimonio.TipoCuentaPatrimonio;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class DeudaCalculadorPatrimonioDeudor implements CalculadorPatrimonio {
    private final DeudasService deudasService;

    @Override
    public double calcular(UUID jugadorId) {
        return -1 * deudasService.findByDeudorJugadorIdPendiente(jugadorId).stream()
                .mapToDouble(Deuda::getPixelcoinsRestantesDePagar)
                .sum();
    }

    @Override
    public TipoCuentaPatrimonio tipoCuenta() {
        return TipoCuentaPatrimonio.DEUDA_DEUDOR;
    }
}
