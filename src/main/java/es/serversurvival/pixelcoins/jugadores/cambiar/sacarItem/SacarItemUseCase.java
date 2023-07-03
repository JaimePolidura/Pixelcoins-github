package es.serversurvival.pixelcoins.jugadores.cambiar.sacarItem;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.transacciones.domain.Transaccion;
import es.serversurvival.pixelcoins.transacciones.application.TransaccionesSaver;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class SacarItemUseCase implements UseCaseHandler<SacarItemParametros> {
    private final TransaccionesSaver transaccionesSaver;
    private final JugadoresService jugadoresService;
    private final Validador validador;
    private final EventBus eventBus;

    @Override
    public void handle(SacarItemParametros parametros) {
        var jugador = this.jugadoresService.getById(parametros.getJugadorId());
        var pixelcoinsASacar = parametros.getTipoCambio().cambio * parametros.getCantidad();

        validador.jugadorTienePixelcoins(parametros.getJugadorId(), pixelcoinsASacar);

        transaccionesSaver.save(Transaccion.builder()
                .tipo(TipoTransaccion.JUGADORES_CAMBIO_SACAR_ITEM)
                .pagadorId(jugador.getJugadorId())
                .pixelcoins(pixelcoinsASacar)
                .objeto(parametros.getTipoCambio().name())
                .build());

        this.eventBus.publish(new ItemSacadoEvento(jugador, parametros.getTipoCambio().name(), pixelcoinsASacar));
    }
}
