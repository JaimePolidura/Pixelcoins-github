package es.serversurvival.pixelcoins.retos._shared.retos.infrastructure;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.Reto;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.RetosRepository;

import java.util.Optional;

@MySQLRepository
public final class MySQLRetosRepository extends Repository<Reto, Integer, Object> implements RetosRepository {
    private static final String TABLE_NAME = "retos";
    private static final String FIELD_ID = "retoId";

    public MySQLRetosRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public Optional<Reto> findById(int retoId) {
        return super.findById(retoId);
    }

    @Override
    public EntityMapper<Reto, Object> entityMapper() {
        return EntityMapper.builder()
                .table(TABLE_NAME)
                .idField(FIELD_ID)
                .classesToMap(Reto.class)
                .build();
    }
}
