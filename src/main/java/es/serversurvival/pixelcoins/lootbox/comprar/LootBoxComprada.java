package es.serversurvival.pixelcoins.lootbox.comprar;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxTier;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class LootBoxComprada extends PixelcoinsEvento {
    @Getter private final UUID jugadorId;
    @Getter private final LootboxTier tier;
}
