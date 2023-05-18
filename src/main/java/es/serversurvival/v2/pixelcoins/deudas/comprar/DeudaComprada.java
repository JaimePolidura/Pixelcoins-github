package es.serversurvival.v2.pixelcoins.deudas.comprar;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class DeudaComprada extends PixelcoinsEvento {
    @Getter private final UUID deudaId;
    @Getter private final UUID compradorJugadorId;
    @Getter private final double pixelcoins;
}
