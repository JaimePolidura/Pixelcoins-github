package es.serversurvival.pixelcoins.bolsa._shared.posiciones.infrastructure;

import es.dependencyinjector.dependencies.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Order;
import es.jaimetruman.select.Select;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.Posicion;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.PosicionesRepository;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.TipoBolsaApuesta;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.TipoPosicion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class MySQLPosicionesRepository extends DataBaseRepository<Posicion, UUID> implements PosicionesRepository {
    private static final String TABLE_NAME = "posiciones";
    private static final String FIELD_ID = "posicionId";

    public MySQLPosicionesRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(Posicion posicion) {
        super.save(posicion);
    }

    @Override
    public List<Posicion> findByJugadorId(UUID jugadorId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("jugadorId").equal(jugadorId));
    }

    @Override
    public Optional<Posicion> findById(UUID posicionId) {
        return super.findById(posicionId);
    }

    @Override
    public void deleteById(UUID posicionId) {
        super.deleteById(posicionId);
    }

    @Override
    public List<Posicion> findPosicionesCerradasSortByRentabilidad() {
        return super.buildListFromQuery(Select.from(TABLE_NAME)
                .where("tipoPosicion").equal(TipoPosicion.CERRADO)
                .orderBy("rentabilidad", Order.DESC));
    }

    @Override
    protected EntityMapper<Posicion> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .idField(FIELD_ID)
                .classesToMap(Posicion.class)
                .build();
    }

    @Override
    public Posicion buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Posicion(
                UUID.fromString(rs.getString("posicionId")),
                UUID.fromString(rs.getString("activoBolsaId")),
                UUID.fromString(rs.getString("jugadorId")),
                rs.getInt("cantidad"),
                TipoBolsaApuesta.valueOf(rs.getString("tipoApuesta")),
                TipoPosicion.valueOf(rs.getString("tipoPosicion")),
                rs.getDouble("precioApertura"),
                rs.getTimestamp("fechaApertura").toLocalDateTime(),
                rs.getDouble("precioCierre"),
                rs.getTimestamp("fechaCierre") != null ? rs.getTimestamp("fechaCierre").toLocalDateTime() : null,
                rs.getDouble("rentabilidad")
        );
    }
}
