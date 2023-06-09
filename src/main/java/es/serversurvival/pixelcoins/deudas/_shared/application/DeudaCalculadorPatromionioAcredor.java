package es.serversurvival.pixelcoins.deudas._shared.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.jugadores.patrimonio.CalculadorPatrimonio;
import es.serversurvival.pixelcoins.jugadores.patrimonio.TipoCuentaPatrimonio;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class DeudaCalculadorPatromionioAcredor implements CalculadorPatrimonio {
    private final DeudasService deudasService;

    @Override
    public double calcular(UUID jugadorId) {
        return deudasService.findByAcredorJugadorIdPendiente(jugadorId).stream()
                .mapToDouble(Deuda::getPixelcoinsRestantesDePagar)
                .sum();
    }

    @Override
    public TipoCuentaPatrimonio tipoCuenta() {
        return TipoCuentaPatrimonio.DEUDA_ACREDOR;
    }
}
