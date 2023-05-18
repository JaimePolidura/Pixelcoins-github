package es.serversurvival.v2.pixelcoins.mercado.comprar;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class ComprarOfertaUseCaseParametros {
    @Getter private final UUID ofertaId;
    @Getter private final UUID jugadorId;
}
