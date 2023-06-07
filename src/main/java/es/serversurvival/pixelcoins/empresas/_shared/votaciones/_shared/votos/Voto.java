package es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
public final class Voto {
    @Getter private final UUID votoId;
    @Getter private final UUID votacionId;
    @Getter private final UUID jugadorId;
    @Getter private final boolean afavor;
    @Getter private final int nAcciones;
    @Getter private final LocalDateTime fechaVotacion;

    public Voto decrementarNAccionesEnUno() {
        return new Voto(votoId, votacionId, jugadorId, afavor, nAcciones - 1, fechaVotacion);
    }
}
