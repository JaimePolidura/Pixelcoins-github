package es.serversurvival.pixelcoins.jugadores._shared.jugadores;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.jugadores.patrimonio.CalculadorPatrimonio;
import es.serversurvival.pixelcoins.jugadores.patrimonio.TipoCuentaPatrimonio;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class EfectivoCalculadorPatrimonio implements CalculadorPatrimonio {
    private final TransaccionesService transaccionesService;

    @Override
    public double calcular(UUID jugadorId) {
        return transaccionesService.getBalancePixelcions(jugadorId);
    }

    @Override
    public TipoCuentaPatrimonio tipoCuenta() {
        return TipoCuentaPatrimonio.EFECTIVO;
    }
}
