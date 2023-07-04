package es.serversurvival.pixelcoins.retos._shared.retosadquiridos.infrastructure;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.pixelcoins.retos._shared.retosadquiridos.domain.RetoAdquirido;
import es.serversurvival.pixelcoins.retos._shared.retosadquiridos.domain.RetosAdquiridosRepository;

import java.util.Optional;
import java.util.UUID;

@MySQLRepository
public final class MySQLRetosAdquiridosRepository extends Repository<RetoAdquirido, UUID, Object> implements RetosAdquiridosRepository {
    private static final String TABLE_NAME = "retos_adquiridos";
    private static final String FIELD_ID = "retoAdquiridoId";

    public MySQLRetosAdquiridosRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void save(RetoAdquirido retoAdquirido) {
        super.save(retoAdquirido);
    }

    @Override
    public Optional<RetoAdquirido> findByJugadorIdAndRetoId(UUID jugadorId, UUID retoId) {
        return super.buildObjectFromQuery(Select.from(TABLE_NAME).where("jugadorId").equal(jugadorId)
                .and("retoId").equal(retoId));
    }

    @Override
    public EntityMapper<RetoAdquirido, Object> entityMapper() {
        return EntityMapper.builder()
                .idField(FIELD_ID)
                .table(TABLE_NAME)
                .classesToMap(RetoAdquirido.class)
                .build();
    }
}
