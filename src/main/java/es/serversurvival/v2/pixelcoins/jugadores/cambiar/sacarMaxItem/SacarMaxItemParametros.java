package es.serversurvival.v2.pixelcoins.jugadores.cambiar.sacarMaxItem;

import es.serversurvival.v2.pixelcoins.jugadores._shared.Jugador;
import es.serversurvival.v2.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class SacarMaxItemParametros {
    @Getter private final Jugador jugador;
    @Getter private final TipoCambioPixelcoins tipoCambio;
}
