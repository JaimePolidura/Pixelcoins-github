package es.serversurvival.bolsa.ordenespremarket._shared.infrastructure;

import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.OrdenPremarket;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.OrderesPremarketRepository;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public final class MySQLOrdenesPremarketRepository extends DataBaseRepository<OrdenPremarket, UUID> implements OrderesPremarketRepository {
    private static final String TABLE_NAME = "ordenespremarket";
    private static final String ID_FIELD_NAME = "orderPremarketId";

    public MySQLOrdenesPremarketRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(OrdenPremarket orden) {
        super.save(orden);
    }

    @Override
    public List<OrdenPremarket> findAll() {
        return super.all();
    }

    @Override
    public List<OrdenPremarket> findByJugador(String jugador) {
        return buildListFromQuery(Select.from(TABLE_NAME).where("jugador").equal(jugador));
    }

    @Override
    public void deleteById(UUID id) {
        super.deleteById(id);
    }

    @Override
    protected EntityMapper<OrdenPremarket> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .classToMap(OrdenPremarket.class)
                .idField(ID_FIELD_NAME)
                .build();
    }

    @Override
    public OrdenPremarket buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new OrdenPremarket(
                UUID.fromString(rs.getString("id")),
                rs.getString("jugador"),
                rs.getString("nombre_activo"),
                rs.getInt("cantidad"),
                TipoAccion.valueOf(rs.getString("accion_orden")),
                UUID.fromString(rs.getString("id_posicionabierta"))
        );
    }
}
