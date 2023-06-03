package es.serversurvival.v2.pixelcoins.jugadores.cambiar.ingresarItem;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class ItemIngresadoEvento extends PixelcoinsEvento {
    @Getter private final UUID jugadorId;
    @Getter private final double pixelcoins;
    @Getter private final String nombreitem;
}
