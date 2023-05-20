package es.serversurvival.v2.pixelcoins.jugadores.cambiar.sacarItem;

import es.serversurvival.v2.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class SacarItemParametros {
    @Getter private final String jugadorNombre;
    @Getter private final TipoCambioPixelcoins tipoCambio;
    @Getter private final int cantidad;
}
