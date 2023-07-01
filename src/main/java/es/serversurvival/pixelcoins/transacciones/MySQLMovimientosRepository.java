package es.serversurvival.pixelcoins.transacciones;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.select.Order;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@MySQLRepository
public final class MySQLMovimientosRepository extends Repository<Movimiento, UUID, Object> implements MovimientoRepository {
    private static final String TABLE_NAME = "transacciones_movimientos";
    private static final String FIELD_ID = "movimientoId";

    public MySQLMovimientosRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void save(Movimiento movimiento) {
        super.save(movimiento);
    }

    @Override
    public double getBalance(UUID entidadId) {
        return super.getDoubleByQuery(String.format(
                "SELECT SUM(pixelcoins) FROM %s WHERE entidadId = '%s'", TABLE_NAME, entidadId.toString()
        ));
    }

    @Override
    public List<Movimiento> findByEntidadIdOrderByFecha(UUID entidadId, int limit) {
        return buildListFromQuery(
                Select.from(TABLE_NAME).where("entidadId").equal(entidadId)
                        .orderBy("fecha", Order.DESC)
                        .limit(limit)
        );
    }

    @Override
    public EntityMapper<Movimiento, Object> entityMapper() {
        return EntityMapper.builder()
                .idField(FIELD_ID)
                .table(TABLE_NAME)
                .classesToMap(Movimiento.class)
                .build();
    }
}
