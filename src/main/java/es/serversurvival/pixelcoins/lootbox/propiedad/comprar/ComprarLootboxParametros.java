package es.serversurvival.pixelcoins.lootbox.propiedad.comprar;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import es.serversurvival.pixelcoins.lootbox.items.domain.LootboxTier;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class ComprarLootboxParametros implements ParametrosUseCase {
    @Getter private final UUID jugadorId;
    @Getter private final LootboxTier lootboxTier;
}
