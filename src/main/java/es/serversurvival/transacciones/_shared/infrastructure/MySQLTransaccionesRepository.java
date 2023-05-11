package es.serversurvival.transacciones._shared.infrastructure;

import es.dependencyinjector.dependencies.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import es.serversurvival.transacciones._shared.domain.TransaccionesRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static es.serversurvival.transacciones._shared.domain.TipoTransaccion.valueOf;

@Repository
public final class MySQLTransaccionesRepository extends DataBaseRepository<Transaccion, UUID> implements TransaccionesRepository {
    private static final String TABLE_NAME = "transacciones";
    private static final String ID_TABLE_NAME = "transaccionId";

    public MySQLTransaccionesRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(Transaccion transaccion) {
        super.save(transaccion);
    }

    @Override
    public List<Transaccion> findByJugador(String jugador) {
        return buildListFromQuery(Select.from(TABLE_NAME)
                .where("comprador").equal(jugador)
                .or("vendedor").equal(jugador).build());
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
                UUID.fromString(rs.getString("id")),
                rs.getString("fecha"),
                rs.getString("comprador"),
                rs.getString("vendedor"),
                rs.getInt("cantidad"),
                rs.getString("objeto"),
                valueOf(rs.getString("tipo"))
        );
    }
}
