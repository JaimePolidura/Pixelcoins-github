package es.serversurvival.pixelcoins.bolsa._shared.activos.infrastructure;

import es.dependencyinjector.dependencies.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsaRepository;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class MySQLActivoBolsaRepository extends DataBaseRepository<ActivoBolsa, UUID> implements ActivoBolsaRepository {
    private static final String TABLE_NAME = "bolsa_activos";
    private static final String FIELD_ID = "activoBolsaId";

    public MySQLActivoBolsaRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(ActivoBolsa activoBolsa) {
        super.save(activoBolsa);
    }

    @Override
    public Optional<ActivoBolsa> findById(UUID activoInfoId) {
        return super.findById(activoInfoId);
    }

    @Override
    public Optional<ActivoBolsa> findByNombreCortoAndTipoActivo(String nombreCorto, TipoActivoBolsa tipoActivo) {
        return super.buildObjectFromQuery(Select.from(TABLE_NAME)
                .where("nombreCorto").equal(nombreCorto)
                .and("tipoActivo").equal(tipoActivo));
    }

    @Override
    public List<ActivoBolsa> findByTipo(TipoActivoBolsa tipoActivoBolsa) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("tipoActivo").equal(tipoActivoBolsa));
    }

    @Override
    public List<ActivoBolsa> findAllNReferenciasMayorQue0() {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("nReferencias").bigger(0));
    }

    @Override
    protected EntityMapper<ActivoBolsa> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .idField(FIELD_ID)
                .classesToMap(ActivoBolsa.class)
                .build();
    }

    @Override
    public ActivoBolsa buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new ActivoBolsa(
                UUID.fromString(rs.getString("activoBolsaId")),
                TipoActivoBolsa.valueOf("tipoActivo"),
                rs.getString("nombreCorto"),
                rs.getString("nombreLargo"),
                rs.getInt("nReferencias")
        );
    }
}
