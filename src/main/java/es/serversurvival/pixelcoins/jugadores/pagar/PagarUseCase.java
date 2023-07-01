package es.serversurvival.pixelcoins.jugadores.pagar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.transacciones.Movimiento;
import es.serversurvival.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.Transaccion;
import es.serversurvival.pixelcoins.transacciones.TransaccionesSaver;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class PagarUseCase implements UseCaseHandler<HacerPagarParametros> {
    private final TransaccionesSaver transaccionesSaver;
    private final Validador validador;
    private final EventBus eventBus;

    @Override
    public void handle(HacerPagarParametros parametros) {
        validador.jugadorTienePixelcoins(parametros.getPagadorId(), parametros.getPixelcoins());
        validador.numeroMayorQueCero(parametros.getPixelcoins(), "Las pixelcoins");
        validador.notEqual(parametros.getPagadorId(), parametros.getPagadoId(), "Tu eres tonto o q te pasa?!");

        transaccionesSaver.save(Transaccion.builder()
                .pagadorId(parametros.getPagadorId())
                .pagadoId(parametros.getPagadoId())
                .pixelcoins(parametros.getPixelcoins())
                .tipo(TipoTransaccion.JUGADORES_PAGO)
                .build());

        eventBus.publish(new JugadorPagadoManualmente(parametros.getPixelcoins(), parametros.getPagadorId(), parametros.getPagadoId()));
    }
}
