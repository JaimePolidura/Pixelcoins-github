package es.serversurvival.pixelcoins.transacciones;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;

import java.util.List;
import java.util.UUID;

@MySQLRepository
public final class MySQLTransaccionesRepository extends Repository<Transaccion, UUID, Object> implements TransaccionesRepository {
    private static final String TABLE_NAME = "transacciones";
    private static final String ID_FIELD = "transaccionId";

    public MySQLTransaccionesRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void save(Transaccion transaccion) {
        super.save(transaccion);
    }

    @Override
    public List<Transaccion> findByPagadorId(UUID pagadorId) {
        return buildListFromQuery(
                Select.from(TABLE_NAME).where("pagadorId").equal(pagadorId)
        );
    }

    @Override
    public List<Transaccion> findByPagadoId(UUID pagadorId) {
        return buildListFromQuery(
                Select.from(TABLE_NAME).where("pagadoId").equal(pagadorId)
        );
    }

    @Override
    public List<Transaccion> findByPagadorAndTipo(UUID pagadorId, TipoTransaccion tipo) {
        return buildListFromQuery(
                Select.from(TABLE_NAME)
                        .where("pagadorId").equal(pagadorId)
                        .and("tipo").equal(tipo)
        );
    }

    @Override
    public List<Transaccion> findByPagadoAndTipo(UUID pagadoId, TipoTransaccion tipo) {
        return buildListFromQuery(
                Select.from(TABLE_NAME)
                        .where("pagadoId").equal(pagadoId)
                        .and("tipo").equal(tipo)
        );
    }

    @Override
    public EntityMapper<Transaccion, Object> entityMapper() {
        return EntityMapper.builder()
                .idField(ID_FIELD)
                .table(TABLE_NAME)
                .classesToMap(Transaccion.class)
                .build();
    }
}
