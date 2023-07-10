package es.serversurvival.pixelcoins.lootbox._shared.items.infrastructure;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxItem;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxItemRareza;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxItemsRepository;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxTier;

import java.util.List;
import java.util.UUID;

@MySQLRepository
public final class MySQLLootboxItemsRepository extends Repository<LootboxItem, UUID, Object> implements LootboxItemsRepository {
    private static final String TABLE_NAME = "lootboxes_items";
    private static final String FIELD_ID = "lootboxItemId";

    public MySQLLootboxItemsRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void save(LootboxItem item) {
        super.save(item);
    }

    @Override
    public List<LootboxItem> findByTier(LootboxTier tier) {
        return super.buildListFromQuery(
                Select.from(TABLE_NAME).where("tier").equal(tier)
        );
    }

    @Override
    public List<LootboxItem> findByTierLimitSorByRandom(LootboxTier tier, int limit) {
        return super.buildListFromQuery(String.format(
                "SELECT * FROM %s WHERE tier = '%s' ORDER BY RAND() LIMIT %s", TABLE_NAME, tier, limit
        ));
    }

    @Override
    public List<LootboxItem> findByTierAndRareza(LootboxTier tier, LootboxItemRareza rareza) {
        return super.buildListFromQuery(
                Select.from(TABLE_NAME).where("tier").equal(tier).
                        and("rareza").equal(rareza)
        );
    }

    @Override
    public EntityMapper<LootboxItem, Object> entityMapper() {
        return EntityMapper.builder()
                .idField(FIELD_ID)
                .table(TABLE_NAME)
                .classesToMap(LootboxItem.class)
                .build();
    }
}
