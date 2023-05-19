package es.serversurvival.v2.pixelcoins.deudas.ponerventa;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class DeudaPuestaVenta extends PixelcoinsEvento {
    @Getter private final UUID deudaId;
    @Getter private final UUID ofertaId;
    @Getter private final double precio;
}
