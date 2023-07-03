package es.serversurvival.pixelcoins.empresas._shared.votaciones.iniciar;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.retos._shared.retos.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class VotacionIniciada extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final UUID votacionId;
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaId;

    @Override
    public Map<UUID, RetoMapping> retosByJugadorId() {
        return Map.of(
                jugadorId, RetoMapping.EMPRESAS_ACCIONISTAS_INICIAR_VOTACION
        );
    }
}
