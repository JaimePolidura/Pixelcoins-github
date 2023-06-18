package es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class Voto {
    @Getter private UUID votoId;
    @Getter private UUID votacionId;
    @Getter private UUID jugadorId;
    @Getter private boolean afavor;
    @Getter private int nAcciones;
    @Getter private LocalDateTime fechaVotacion;

    public Voto decrementarNAccionesEnUno() {
        return new Voto(votoId, votacionId, jugadorId, afavor, nAcciones - 1, fechaVotacion);
    }
}
