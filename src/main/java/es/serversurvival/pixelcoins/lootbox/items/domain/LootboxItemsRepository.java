package es.serversurvival.pixelcoins.lootbox.items.domain;

import java.util.List;

public interface LootboxItemsRepository {
    void save(LootboxItem item);

    List<LootboxItem> findByTier(LootboxTier tier);

    List<LootboxItem> findByTierAndRareza(LootboxTier tier, LootboxItemRareza rareza);

    List<LootboxItem> findAll();
}
