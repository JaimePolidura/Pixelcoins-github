package es.serversurvival.pixelcoins.lootbox._shared.propiedad.infrastructure;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.pixelcoins.lootbox._shared.propiedad.domain.LootboxCompradaEstado;
import es.serversurvival.pixelcoins.lootbox._shared.propiedad.domain.LootboxEnPropiedad;
import es.serversurvival.pixelcoins.lootbox._shared.propiedad.domain.LootboxEnPropiedadRepository;

import java.util.List;
import java.util.UUID;

@MySQLRepository
public final class MySQLLootboxEnPropiedadRepository extends Repository<LootboxEnPropiedad, UUID, Object> implements LootboxEnPropiedadRepository {
    private static final String TABLE_NAME = "lootboxes_propiedad";
    private static final String FIELD_ID = "lootboxEnPropiedadId";

    public MySQLLootboxEnPropiedadRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void save(LootboxEnPropiedad lootboxEnPropiedad) {
        super.save(lootboxEnPropiedad);
    }

    @Override
    public List<LootboxEnPropiedad> findByJugadorAndEstado(UUID jugadorId, LootboxCompradaEstado estado) {
        return super.buildListFromQuery(
                Select.from(TABLE_NAME).where("jugadorId").equal(jugadorId)
                        .and("estado").equal(estado)
        );
    }

    @Override
    public EntityMapper<LootboxEnPropiedad, Object> entityMapper() {
        return EntityMapper.builder()
                .table(TABLE_NAME)
                .idField(FIELD_ID)
                .classesToMap(LootboxEnPropiedad.class)
                .build();
    }
}
