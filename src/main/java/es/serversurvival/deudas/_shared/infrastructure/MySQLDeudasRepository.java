package es.serversurvival.deudas._shared.infrastructure;

import es.dependencyinjector.dependencies.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;
import es.serversurvival.deudas._shared.domain.Deuda;
import es.serversurvival.deudas._shared.domain.DeudasRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class MySQLDeudasRepository extends DataBaseRepository<Deuda, UUID> implements DeudasRepository {
    private static final String TABLE_NAME = "deudas";
    private static final String ID_FIELD_NAME = "deudaId";

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
    public List<Deuda> findByAcredor(String acredor) {
        return buildListFromQuery(Select.from(TABLE_NAME).where("acredor").equal(acredor));
    }

    @Override
    public List<Deuda> findByDeudor(String deudor) {
        return buildListFromQuery(Select.from(TABLE_NAME).where("deudor").equal(deudor));
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
                .idField(ID_FIELD_NAME)
                .classesToMap(Deuda.class)
                .build();
    }

    @Override
    public Deuda buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Deuda(
                UUID.fromString(rs.getString(ID_FIELD_NAME)),
                rs.getString("deudor"),
                rs.getString("acredor"),
                rs.getInt("pixelcoinsRestantes"),
                rs.getInt("tiempoRestante"),
                rs.getInt("interes"),
                rs.getInt("cuota"),
                rs.getString("fechaUltimapaga")
        );
    }
}
