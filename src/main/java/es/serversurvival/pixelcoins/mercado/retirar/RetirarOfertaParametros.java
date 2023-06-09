package es.serversurvival.pixelcoins.mercado.retirar;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class RetirarOfertaParametros implements ParametrosUseCase {
    @Getter private final UUID retiradorId;
    @Getter private final UUID ofertaId;

    public static RetirarOfertaParametros of(UUID retiradorId, UUID ofertaId) {
        return new RetirarOfertaParametros(retiradorId, ofertaId);
    }
}
