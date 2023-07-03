package es.serversurvival.pixelcoins.retos._shared.retosadquiridos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class RetoAdquirido {
    @Getter private final UUID jugadorId;
    @Getter private final int retoId;

    public static RetoAdquirido of(UUID jugadorId, int retoId) {
        return new RetoAdquirido(jugadorId, retoId);
    }
}
