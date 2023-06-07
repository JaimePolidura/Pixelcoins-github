package es.serversurvival.pixelcoins.jugadores.pagar;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class JugadorPagadoManualmente extends PixelcoinsEvento {
    @Getter private final double pixelcoins;
    @Getter private final UUID jugadorPagadorId;
    @Getter private final UUID jugadorPagadoId;
}
