package es.serversurvival.v2.pixelcoins.jugadores.cambiar.sacarItem;

import es.jaime.EventBus;
import es.serversurvival.v1._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.v2.pixelcoins.jugadores._shared.Jugador;
import es.serversurvival.v2.pixelcoins.jugadores._shared.JugadoresService;
import es.serversurvival.v2.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import es.serversurvival.v2.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.v2.pixelcoins.transacciones.Transaccion;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class SacarItemUseCase {
    private final TransaccionesService transaccionesService;
    private final JugadoresService jugadoresService;
    private final EventBus eventBus;

    public void sacarItem(SacarItemParametros parametros) {
        var jugador = this.jugadoresService.getByNombre(parametros.getJugadorNombre());
        var pixelcoinsASacar = parametros.getTipoCambio().cambio * parametros.getCantidad();

        asegurarseQueTengaPixelcoins(jugador, pixelcoinsASacar);

        transaccionesService.save(Transaccion.builder()
                .tipo(TipoTransaccion.JUGADORES_CAMBIO_SACAR_ITEM)
                .pagadorId(jugador.getJugadorId())
                .pixelcoins(pixelcoinsASacar)
                .objeto(parametros.getTipoCambio().name())
                .build());

        this.eventBus.publish(new ItemSacadoEvento(jugador, parametros.getTipoCambio().name(), pixelcoinsASacar));
    }

    private void asegurarseQueTengaPixelcoins(Jugador jugador, double pixelcoinsASacar) {
        if(pixelcoinsASacar > this.transaccionesService.getBalancePixelcions(jugador.getJugadorId())){
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins");
        }
    }
}
