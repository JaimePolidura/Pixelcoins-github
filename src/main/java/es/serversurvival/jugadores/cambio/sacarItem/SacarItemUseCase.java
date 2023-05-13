package es.serversurvival.jugadores.cambio.sacarItem;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival.jugadores.cambio.TipoCambioPixelcoins;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class SacarItemUseCase {
    private final JugadoresService jugadoresService;

    public void sacarItem(Jugador jugador, TipoCambioPixelcoins tipoCambio, int cantidad) {
        this.ensureHasEnoughPixelcoins(jugador, tipoCambio, cantidad);

        double pixelcoinsASacar = tipoCambio.cambio * cantidad;

        jugadoresService.save(jugador.decrementPixelcoinsBy(pixelcoinsASacar));

        Pixelcoin.publish(new ItemSacadoEvento(jugador, tipoCambio.name(), (int) pixelcoinsASacar));
    }

    private void ensureHasEnoughPixelcoins(Jugador jugador, TipoCambioPixelcoins tipoCambio, int cantidad) {
        if(tipoCambio.cambio * cantidad > jugador.getPixelcoins()){
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins para sacar " + tipoCambio.name());
        }
    }
}
