package es.serversurvival.pixelcoins.deudas._shared.infrastructure;

import es.dependencyinjector.dependencies.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.deudas._shared.domain.DeudasRepository;
import es.serversurvival.pixelcoins.deudas._shared.domain.EstadoDeuda;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class MySQLDeudasRepository extends DataBaseRepository<Deuda, UUID> implements DeudasRepository {
    private static final String FIELD_ID = "deudaId";
    private static final String TABLE_NAME = "deudas";

    public MySQLDeudasRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(Deuda deuda) {
        super.save(deuda);
    }

    @Override
    public Optional<Deuda> findById(UUID id) {
        return super.findById(id);
    }

    @Override
    public List<Deuda> findByAcredorJugadorId(UUID acredorJugadorId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("acredorJugadorId").equal(acredorJugadorId));
    }

    @Override
    public List<Deuda> findByDeudorJugadorId(UUID deudorJugadorId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("deudorJugadorId").equal(deudorJugadorId));
    }

    @Override
    public List<Deuda> findByJugadorId(UUID jugadorId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME)
                .where("deudorJugadorId").equal(jugadorId)
                .or("acredorJugadorId").equal(jugadorId));
    }

    @Override
    public List<Deuda> findAll() {
        return super.all();
    }

    @Override
    public void deleteById(UUID id) {
        super.deleteById(id);
    }

    @Override
    protected EntityMapper<Deuda> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .idField(FIELD_ID)
                .classesToMap(Deuda.class)
                .build();
    }

    @Override
    public Deuda buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Deuda(
                UUID.fromString(rs.getString("deudaId")),
                UUID.fromString(rs.getString("acredorJugadorId")),
                UUID.fromString(rs.getString("deudorJugadorId")),
                rs.getDouble("nominal"),
                rs.getDouble("interes"),
                rs.getInt("nCuotasTotales"),
                rs.getInt("nCuotasPagadas"),
                rs.getInt("nCuotasImpagadas"),
                rs.getString("fechaUltimoPagoCuota") != null ? Timestamp.valueOf(rs.getString("fechaUltimoPagoCuota")).toLocalDateTime() : null,
                Timestamp.valueOf(rs.getString("fechaCreacion")).toLocalDateTime(),
                rs.getLong("periodoPagoCuotaMs"),
                EstadoDeuda.valueOf(rs.getString("haSidoPagadoAlCompleto"))
        );
    }
}
