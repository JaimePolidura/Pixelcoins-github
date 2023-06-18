package es.serversurvival.pixelcoins.bolsa._shared.premarket.infrastructure;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.delete.Delete;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.pixelcoins.bolsa._shared.premarket.domain.OrdenPremarket;
import es.serversurvival.pixelcoins.bolsa._shared.premarket.domain.OrdenesPremarketRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MySQLRepository
public final class MySQLOrdenesPremarketRepository extends Repository<OrdenPremarket, UUID, Object> implements OrdenesPremarketRepository {
    private static final String TABLE_NAME = "bolsa_ordenes_premarket";
    private static final String FIELD_ID = "ordenPremarketId";

    public MySQLOrdenesPremarketRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void save(OrdenPremarket orden) {
        super.save(orden);
    }

    @Override
    public Optional<OrdenPremarket> findById(UUID ordenPremarketId) {
        return super.findById(ordenPremarketId);
    }

    @Override
    public List<OrdenPremarket> findByJugadorId(UUID jugadorId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("jugadorId").equal(jugadorId));
    }

    @Override
    public List<OrdenPremarket> findByPosicionAbiertaId(UUID posicionAbiertaId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("posicionAbiertaId").equal(posicionAbiertaId));
    }

    @Override
    public List<OrdenPremarket> findAll() {
        return super.findAll();
    }

    @Override
    public void deleteById(UUID ordenPremarketId) {
        super.deleteById(ordenPremarketId);
    }

    @Override
    public void deletebyPosicionAbiertId(UUID posicionAbiertaId) {
        super.execute(Delete.from(TABLE_NAME).where("posicionAbiertaId").equal(posicionAbiertaId));
    }

    @Override
    public EntityMapper<OrdenPremarket, Object> entityMapper() {
        return EntityMapper.builder()
                .table(TABLE_NAME)
                .idField(FIELD_ID)
                .classesToMap(OrdenPremarket.class)
                .build();
    }
}
