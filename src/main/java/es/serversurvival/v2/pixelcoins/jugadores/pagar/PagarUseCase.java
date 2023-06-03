package es.serversurvival.v2.pixelcoins.jugadores.pagar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins._shared.Validador;
import es.serversurvival.v2.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.v2.pixelcoins.transacciones.Transaccion;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class PagarUseCase {
    private final TransaccionesService transaccionesService;
    private final Validador validador;
    private final EventBus eventBus;

    public void hacerPago(HacerPagarParametros parametros) {
        validador.jugadorTienePixelcoins(parametros.getPagadorId(), parametros.getPixelcoins());
        validador.numeroMayorQueCero(parametros.getPixelcoins(), "Las pixelcoins");
        validador.notEqual(parametros.getPagadorId(), parametros.getPagadoId(), "Tu eres tonto o q te pasa?!");

        transaccionesService.save(Transaccion.builder()
                        .pagadorId(parametros.getPagadorId())
                        .pagadoId(parametros.getPagadoId())
                        .pixelcoins(parametros.getPixelcoins())
                        .tipo(TipoTransaccion.JUGADORES_PAGO)
                .build());

        eventBus.publish(new JugadorPagadoManualmente(parametros.getPixelcoins(), parametros.getPagadorId(), parametros.getPagadoId()));
    }
}
