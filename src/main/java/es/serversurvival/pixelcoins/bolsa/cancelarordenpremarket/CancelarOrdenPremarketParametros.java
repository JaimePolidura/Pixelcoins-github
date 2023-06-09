package es.serversurvival.pixelcoins.bolsa.cancelarordenpremarket;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class CancelarOrdenPremarketParametros implements ParametrosUseCase {
    @Getter private final UUID jugadorId;
    @Getter private final UUID ordenPremarketId;
}
