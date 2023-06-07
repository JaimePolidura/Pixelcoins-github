package es.serversurvival.pixelcoins.empresas.dejarempleo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class DejarEmploParametros {
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaId;
}
