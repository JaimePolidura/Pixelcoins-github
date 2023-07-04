package es.serversurvival.minecraftserver.jugadores.venderjugador;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class ItemVendidoJugadorEvento extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final UUID compradorJugadorId;
    @Getter private final UUID vendedorJugadorId;
    @Getter private final double pixelcoins;
    @Getter private final String itemMaterial;

    @Override
    public Map<UUID, List<RetoMapping>> retosByJugadorId() {
        return Map.of(
                compradorJugadorId, List.of(RetoMapping.JUGADORES_VENDER_JUGADOR_COMPRADOR),
                vendedorJugadorId, List.of(RetoMapping.JUGADORES_VENDER_JUGADOR_VENDEDOR)
        );
    }
}
