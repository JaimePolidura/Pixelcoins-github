package es.serversurvival.pixelcoins.lootbox.items.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.lootbox.items.domain.LootboxTier;
import es.serversurvival.pixelcoins.lootbox.items.domain.LootboxItem;
import es.serversurvival.pixelcoins.lootbox.items.domain.LootboxItemRareza;
import es.serversurvival.pixelcoins.lootbox.items.domain.LootboxItemsRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@Service
@AllArgsConstructor
public final class LootboxItemsService {
    private final LootboxItemsRepository repository;

    public void save(LootboxItem item) {
        repository.save(item);
    }

    public List<LootboxItem> findByTier(LootboxTier tier) {
        return repository.findByTier(tier);
    }

    public List<LootboxItem> findByTierAndRareza(LootboxTier tier, LootboxItemRareza rareza) {
        return repository.findByTierAndRareza(tier, rareza);
    }
}
