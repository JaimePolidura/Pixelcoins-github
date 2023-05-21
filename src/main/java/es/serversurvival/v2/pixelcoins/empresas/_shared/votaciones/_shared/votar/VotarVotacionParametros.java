package es.serversurvival.v2.pixelcoins.empresas._shared.votaciones._shared.votar;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class VotarVotacionParametros {
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaId;
    @Getter private final UUID votacionId;
    @Getter private final boolean aFavor;
}
