package es.serversurvival.pixelcoins.mercado.comprar;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class ComprarOfertaParametros implements ParametrosUseCase {
    @Getter private final UUID jugadorId;
    @Getter private final UUID ofertaId;

    public static ComprarOfertaParametros of(UUID jugadorId, UUID ofertaId) {
        return new ComprarOfertaParametros(jugadorId, ofertaId);
    }
}
