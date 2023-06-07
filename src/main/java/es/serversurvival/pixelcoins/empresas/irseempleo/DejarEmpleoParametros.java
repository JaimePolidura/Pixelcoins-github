package es.serversurvival.pixelcoins.empresas.irseempleo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class DejarEmpleoParametros {
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaId;
}
