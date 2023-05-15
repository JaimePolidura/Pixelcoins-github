package es.serversurvival.v2.pixelcoins.jugadores.cambiar.ingresarItem;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.v2.pixelcoins.jugadores._shared.Jugador;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ItemIngresadoEvento extends PixelcoinsEvento {
    @Getter private final Jugador jugador;
    @Getter private final double pixelcoins;
    @Getter private final String nombreitem;
}
