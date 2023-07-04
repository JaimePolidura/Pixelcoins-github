package es.serversurvival.pixelcoins.tienda.comprar;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class TiendaItemMinecraftVendido extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final UUID compradorJugadorId;
    @Getter private final UUID vendedorJugadorId;
    @Getter private final double pixelcoins;

    @Override
    public Map<UUID, List<RetoMapping>> retosByJugadorId() {
        return Map.of(vendedorJugadorId, List.of(
                RetoMapping.TIENDA_VENDER_VOLUMEN,
                RetoMapping.TIENDA_VENDER_CANTIDAD
        ));
    }
}
