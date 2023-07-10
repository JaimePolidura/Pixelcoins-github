package es.serversurvival.pixelcoins.lootbox.abrir;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class AbrirLootboxParametros implements ParametrosUseCase {
    @Getter private final UUID lootboxEnPropiedadId;
    @Getter private final UUID jugadorId;
}
