package es.serversurvival.v2.pixelcoins.jugadores.cambiar.sacarItem;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.v2.pixelcoins.jugadores._shared.jugadores.Jugador;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ItemSacadoEvento extends PixelcoinsEvento {
    @Getter private final Jugador jugador;
    @Getter private final String itemNombre;
    @Getter private final int pixelcoins;
}
