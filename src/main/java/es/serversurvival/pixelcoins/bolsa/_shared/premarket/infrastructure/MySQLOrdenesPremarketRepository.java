package es.serversurvival.pixelcoins.bolsa._shared.premarket.infrastructure;

import es.dependencyinjector.dependencies.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.delete.Delete;
import es.jaimetruman.select.Select;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.TipoBolsaApuesta;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.TipoPosicion;
import es.serversurvival.pixelcoins.bolsa._shared.premarket.domain.OrdenPremarket;
import es.serversurvival.pixelcoins.bolsa._shared.premarket.domain.OrdenesPremarketRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class MySQLOrdenesPremarketRepository extends DataBaseRepository<OrdenPremarket, UUID> implements OrdenesPremarketRepository {
    private static final String TABLE_NAME = "bolsa_ordenes_premarket";
    private static final String FIELD_ID = "ordenPremarketId";

    public MySQLOrdenesPremarketRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(OrdenPremarket orden) {
        super.save(orden);
    }

    @Override
    public Optional<OrdenPremarket> findById(UUID ordenPremarketId) {
        return super.findById(ordenPremarketId);
    }

    @Override
    public List<OrdenPremarket> findByJugadorId(UUID jugadorId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("jugadorId").equal(jugadorId));
    }

    @Override
    public List<OrdenPremarket> findByPosicionAbiertaId(UUID posicionAbiertaId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("posicionAbiertaId").equal(posicionAbiertaId));
    }

    @Override
    public List<OrdenPremarket> findAll() {
        return super.all();
    }

    @Override
    public void deleteById(UUID ordenPremarketId) {
        super.deleteById(ordenPremarketId);
    }

    @Override
    public void deletebyPosicionAbiertId(UUID posicionAbiertaId) {
        super.execute(Delete.from(TABLE_NAME).where("posicionAbiertaId").equal(posicionAbiertaId));
    }

    @Override
    protected EntityMapper<OrdenPremarket> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .classesToMap(OrdenPremarket.class)
                .idField(FIELD_ID)
                .build();
    }

    @Override
    public OrdenPremarket buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new OrdenPremarket(
                UUID.fromString(rs.getString("ordenPremarketId")),
                UUID.fromString(rs.getString("jugadorId")),
                rs.getTimestamp("fechaCreacion").toLocalDateTime(),
                TipoPosicion.valueOf(rs.getString("tipoPosicion")),
                rs.getInt("cantidad"),
                TipoBolsaApuesta.valueOf(rs.getString("tipoBolsaApuesta")),
                rs.getString("activoBolsaId") != null ? UUID.fromString(rs.getString("activoBolsaId")) : null,
                rs.getString("posicionAbiertaId") != null ? UUID.fromString(rs.getString("posicionAbiertaId")) : null
        );
    }
}
