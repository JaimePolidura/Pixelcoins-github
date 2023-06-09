package es.serversurvival.pixelcoins.jugadores._shared.estadisticas.infrastructure;

import es.dependencyinjector.dependencies.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Order;
import es.jaimetruman.select.Select;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.domain.JugadorEstadisticas;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.domain.JugadorTipoContadorEstadistica;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.domain.JugadoresEstadisticasRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class MySQLJugadoresEstadisticasRepository extends DataBaseRepository<JugadorEstadisticas, UUID> implements JugadoresEstadisticasRepository {
    private static final String TABLE_NAME = "jugadores_estadisticas";
    private static final String ID_FIELD = "jugadorId";

    protected MySQLJugadoresEstadisticasRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(JugadorEstadisticas estadisticas) {
        super.save(estadisticas);
    }

    @Override
    public Optional<JugadorEstadisticas> findById(UUID jugadorId) {
        return super.findById(jugadorId);
    }

    @Override
    public List<JugadorEstadisticas> sortBy(JugadorTipoContadorEstadistica tipo, boolean creciente) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).orderBy(tipo.getNombreCampo(), creciente ? Order.ASC : Order.DESC));
    }

    @Override
    protected EntityMapper<JugadorEstadisticas> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .classesToMap(JugadorEstadisticas.class)
                .idField(ID_FIELD)
                .build();
    }

    @Override
    public JugadorEstadisticas buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new JugadorEstadisticas(
                UUID.fromString(rs.getString("jugadorId")),
                rs.getInt("nDeudaPagos"),
                rs.getInt("nDeudaInpagos"),
                rs.getInt("nVentasTienda"),
                rs.getDouble("valorPixelcoinsVentasTienda"),
                rs.getInt("nComprasTineda"),
                rs.getDouble("valorPixelcoinsComprasTienda"),
                rs.getInt("numeroCompraVentasBolsa")
        );
    }
}
