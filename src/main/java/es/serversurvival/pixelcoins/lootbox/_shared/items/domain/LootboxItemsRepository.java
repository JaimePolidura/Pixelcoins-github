package es.serversurvival.pixelcoins.lootbox._shared.items.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LootboxItemsRepository {
    void save(LootboxItem item);

    Optional<LootboxItem> findById(UUID lootboxItemId);

    List<LootboxItem> findByTier(LootboxTier tier);

    List<LootboxItem> findByTierLimitSorByRandom(LootboxTier tier, int limit);

    List<LootboxItem> findByTierAndRareza(LootboxTier tier, LootboxItemRareza rareza);

    List<LootboxItem> findAll();
}
