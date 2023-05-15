package es.serversurvival.v1.jugadores.cambio.sacarItem;

import es.jaime.EventBus;
import es.serversurvival.v1._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import es.serversurvival.v1.jugadores.cambio.TipoCambioPixelcoins;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class SacarItemUseCase {
    private final JugadoresService jugadoresService;
    private final EventBus eventBus;

    public void sacarItem(Jugador jugador, TipoCambioPixelcoins tipoCambio, int cantidad) {
        this.ensureHasEnoughPixelcoins(jugador, tipoCambio, cantidad);

        double pixelcoinsASacar = tipoCambio.cambio * cantidad;

        jugadoresService.save(jugador.decrementPixelcoinsBy(pixelcoinsASacar));

        this.eventBus.publish(new ItemSacadoEvento(jugador, tipoCambio.name(), (int) pixelcoinsASacar));
    }

    private void ensureHasEnoughPixelcoins(Jugador jugador, TipoCambioPixelcoins tipoCambio, int cantidad) {
        if(tipoCambio.cambio * cantidad > jugador.getPixelcoins()){
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins para sacar " + tipoCambio.name());
        }
    }
}
