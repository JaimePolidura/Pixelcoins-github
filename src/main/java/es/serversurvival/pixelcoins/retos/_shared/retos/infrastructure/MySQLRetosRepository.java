package es.serversurvival.pixelcoins.retos._shared.retos.infrastructure;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.select.Order;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.ModuloReto;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.Reto;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.RetosRepository;

import java.nio.channels.SeekableByteChannel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MySQLRepository
public final class MySQLRetosRepository extends Repository<Reto, UUID, Object> implements RetosRepository {
    private static final String TABLE_NAME = "retos";
    private static final String FIELD_ID = "retoId";

    public MySQLRetosRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void save(Reto reto) {
        super.save(reto);
    }

    @Override
    public Optional<Reto> findById(UUID retoId) {
        return super.findById(retoId);
    }

    @Override
    public Optional<Reto> findByMapping(RetoMapping retoMapping) {
        return super.buildObjectFromQuery(Select.from(TABLE_NAME).where("mapping").equal(retoMapping));
    }

    @Override
    public List<Reto> findByModuloAndRetoPadreId(ModuloReto modulo, UUID retoPadreId) {
        return super.buildListFromQuery(
                Select.from(TABLE_NAME).where("modulo").equal(modulo.name())
                        .and("retoPadreId").equal(retoPadreId)
        );
    }

    @Override
    public List<Reto> findByRetoPadreProgresionIdSortByPosicion(UUID retoPadreProgresionId) {
        return super.buildListFromQuery(
                Select.from(TABLE_NAME).where("retoPadreProgresionId").equal(retoPadreProgresionId)
                        .orderBy("posicionEnProgresion", Order.ASC)
        );
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
