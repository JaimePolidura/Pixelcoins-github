package es.serversurvival.pixelcoins.retos._shared.retosadquiridos.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public final class RetoAdquirido {
    @Getter private UUID retoAdquiridoId;
    @Getter private UUID jugadorId;
    @Getter private UUID retoId;
    @Getter private LocalDateTime fechaAdquisicion;

    public static RetoAdquirido of(UUID jugadorId, UUID retoId) {
        return new RetoAdquirido(UUID.randomUUID(), jugadorId, retoId, LocalDateTime.now());
    }
}
