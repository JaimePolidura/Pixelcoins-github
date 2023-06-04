package es.serversurvival.v2.pixelcoins.mercado.comprar;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class ComprarOfertaParametros {
    @Getter private final UUID jugadorId;
    @Getter private final UUID ofertaId;

    public static ComprarOfertaParametros of(UUID jugadorId, UUID ofertaId) {
        return new ComprarOfertaParametros(jugadorId, ofertaId);
    }
}
