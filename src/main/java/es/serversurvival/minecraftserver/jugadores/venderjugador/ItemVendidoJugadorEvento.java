package es.serversurvival.minecraftserver.jugadores.venderjugador;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class ItemVendidoJugadorEvento extends PixelcoinsEvento {
    @Getter private final UUID compradorJugadorId;
    @Getter private final UUID vendedorJugadorId;
    @Getter private final double pixelcoins;
    @Getter private final String itemMaterial;
}
