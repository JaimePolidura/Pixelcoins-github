package es.serversurvival.v2.pixelcoins.mercado.retirar;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class RetirarOfertaUseCaseParametros {
    @Getter private final UUID jugadorId;
    @Getter private final UUID ofertaId;
}
