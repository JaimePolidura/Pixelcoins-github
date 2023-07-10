package es.serversurvival.pixelcoins.lootbox._shared.items.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxTier;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxItem;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxItemRareza;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxItemsRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class LootboxItemsService {
    private final LootboxItemsRepository repository;

    public LootboxItem getById(UUID lootboxItemId) {
        return this.repository.findById(lootboxItemId)
                .orElseThrow(() -> new ResourceNotFound("No se ha encontrado el item del lootbox"));
    }

    public List<LootboxItem> findByTierLimitSorByRandom(LootboxTier tier, int limit) {
       return this.repository.findByTierLimitSorByRandom(tier, limit);
    }

    public void save(LootboxItem item) {
        repository.save(item);
    }

    public List<LootboxItem> findByTier(LootboxTier tier) {
        return repository.findByTier(tier);
    }

    public List<LootboxItem> findByTierAndRareza(LootboxTier tier, LootboxItemRareza rareza) {
        return repository.findByTierAndRareza(tier, rareza);
    }

    public List<LootboxItem> findAll() {
        return repository.findAll();
    }
}
