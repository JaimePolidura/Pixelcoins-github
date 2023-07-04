package es.serversurvival.pixelcoins.deudas.comprar;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class DeudaComprada extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final UUID deudaId;
    @Getter private final UUID compradorJugadorId;
    @Getter private final UUID vendedorJugadorId;
    @Getter private final double pixelcoins;

    @Override
    public Map<UUID, List<RetoMapping>> retosByJugadorId() {
        return Map.of(
                compradorJugadorId, List.of(RetoMapping.DEUDAS_COMPRAR),
                vendedorJugadorId, List.of(RetoMapping.DEUDAS_VENDER)
        );
    }
}
