package es.serversurvival.v2.pixelcoins.transacciones;

import es.dependencyinjector.dependencies.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public final class MySQLTransaccionesRepository extends DataBaseRepository<Transaccion, UUID> implements TransaccionesRepository {
    private static final String TABLE_NAME = "transacciones";
    private static final String ID_TABLE_NAME = "transaccionId";

    protected MySQLTransaccionesRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(Transaccion transaccion) {
        super.save(transaccion);
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
    protected EntityMapper<Transaccion> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .classesToMap(Transaccion.class)
                .idField(ID_TABLE_NAME)
                .build();
    }

    @Override
    public Transaccion buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Transaccion(
                UUID.fromString(rs.getString("transaccionId")),
                TipoTransaccion.valueOf(rs.getString("tipo")),
                UUID.fromString(rs.getString("pagadorId")),
                UUID.fromString(rs.getString("pagadoId")),
                rs.getDouble("pixelcoins"),
                Timestamp.valueOf(rs.getString("fecha")).toLocalDateTime(),
                rs.getString("objeto")
        );
    }
}
