package es.serversurvival.pixelcoins.deudas._shared.infrastructure;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.deudas._shared.domain.DeudasRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MySQLRepository
public final class MySQLDeudasRepository extends Repository<Deuda, UUID, Object> implements DeudasRepository {
    private static final String FIELD_ID = "deudaId";
    private static final String TABLE_NAME = "deudas";

    public MySQLDeudasRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void save(Deuda deuda) {
        super.save(deuda);
    }

    @Override
    public Optional<Deuda> findById(UUID id) {
        return super.findById(id);
    }

    @Override
    public List<Deuda> findByAcredorJugadorId(UUID acredorJugadorId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("acredorJugadorId").equal(acredorJugadorId));
    }

    @Override
    public List<Deuda> findByDeudorJugadorId(UUID deudorJugadorId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("deudorJugadorId").equal(deudorJugadorId));
    }

    @Override
    public List<Deuda> findByJugadorId(UUID jugadorId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME)
                .where("deudorJugadorId").equal(jugadorId)
                .or("acredorJugadorId").equal(jugadorId));
    }

    @Override
    public List<Deuda> findAll() {
        return super.findAll();
    }

    @Override
    public void deleteById(UUID id) {
        super.deleteById(id);
    }

    @Override
    public EntityMapper<Deuda, Object> entityMapper() {
        return EntityMapper.builder()
                .classesToMap(Deuda.class)
                .idField(FIELD_ID)
                .table(TABLE_NAME)
                .build();
    }
}
