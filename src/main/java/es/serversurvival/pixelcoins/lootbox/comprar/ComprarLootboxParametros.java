package es.serversurvival.pixelcoins.lootbox.comprar;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxTier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class ComprarLootboxParametros implements ParametrosUseCase {
    @Getter private final UUID jugadorId;
    @Getter private final LootboxTier lootboxTier;
    @Getter private final UUID lootboxPropiedadId; //Necesito saber la id desde fuera
}
