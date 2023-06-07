package es.serversurvival.pixelcoins.deudas._shared;

import es.dependencyinjector.dependencies.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class MySQLDeudasRepository extends DataBaseRepository<Deuda, UUID> implements DeudasRepository {
    protected MySQLDeudasRepository(DatabaseConfiguration databaseConnection) {
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
        return null;
    }

    @Override
    public List<Deuda> findByDeudorJugadorId(UUID deudorJugadorId) {
        return null;
    }

    @Override
    public List<Deuda> findByJugadorId(UUID jugadorId) {
        return null;
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
        return EntityMapper.table("deudas")
                .idField("deudaId")
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
                Timestamp.valueOf(rs.getString("fechaUltimoPagoCuota")).toLocalDateTime(),
                Timestamp.valueOf(rs.getString("fechaCreacion")).toLocalDateTime(),
                rs.getLong("periodoPagoCuotaMs"),
                EstadoDeuda.valueOf(rs.getString("haSidoPagadoAlCompleto"))
        );
    }
}
