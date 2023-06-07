package es.serversurvival.pixelcoins.jugadores.cambiar.sacarMaxItem;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.Jugador;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ItemSacadoMaxEvento extends PixelcoinsEvento {
    @Getter private final Jugador jugador;
    @Getter private final String itemNombre;
    @Getter private final int pixelcoins;
}
