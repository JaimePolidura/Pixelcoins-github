package es.serversurvival.pixelcoins.retos._shared.retosadquiridos.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public final class RetoAdquirido {
    @Getter private UUID retoAdquiridoId;
    @Getter private UUID jugadorId;
    @Getter private int retoId;

    public static RetoAdquirido of(UUID jugadorId, int retoId) {
        return new RetoAdquirido(UUID.randomUUID(), jugadorId, retoId);
    }
}
