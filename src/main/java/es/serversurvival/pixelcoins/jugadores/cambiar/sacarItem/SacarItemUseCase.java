package es.serversurvival.pixelcoins.jugadores.cambiar.sacarItem;

import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.Transaccion;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class SacarItemUseCase {
    private final TransaccionesService transaccionesService;
    private final JugadoresService jugadoresService;
    private final Validador validador;
    private final EventBus eventBus;

    public void sacarItem(SacarItemParametros parametros) {
        var jugador = this.jugadoresService.getById(parametros.getJugadorId());
        var pixelcoinsASacar = parametros.getTipoCambio().cambio * parametros.getCantidad();

        validador.jugadorTienePixelcoins(parametros.getJugadorId(), pixelcoinsASacar);

        transaccionesService.save(Transaccion.builder()
                .tipo(TipoTransaccion.JUGADORES_CAMBIO_SACAR_ITEM)
                .pagadorId(jugador.getJugadorId())
                .pixelcoins(pixelcoinsASacar)
                .objeto(parametros.getTipoCambio().name())
                .build());

        this.eventBus.publish(new ItemSacadoEvento(jugador, parametros.getTipoCambio().name(), pixelcoinsASacar));
    }
}
