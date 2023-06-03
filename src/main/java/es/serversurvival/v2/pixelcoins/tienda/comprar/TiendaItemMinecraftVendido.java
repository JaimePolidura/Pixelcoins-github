package es.serversurvival.v2.pixelcoins.tienda.comprar;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class TiendaItemMinecraftVendido extends PixelcoinsEvento {
    @Getter private final UUID compradorJugadorId;
    @Getter private final UUID vendedorJugadorId;
    @Getter private final double pixelcoins;
}
