package es.serversurvival.v2.pixelcoins.jugadores.cambiar.ingresarItem;

import es.serversurvival.v2.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class IngresarItemParametros {
    @Getter private final String nombreJugador;
    @Getter private final TipoCambioPixelcoins tipoCambioPixelcoins;
    @Getter private final int cantiadad;
}
