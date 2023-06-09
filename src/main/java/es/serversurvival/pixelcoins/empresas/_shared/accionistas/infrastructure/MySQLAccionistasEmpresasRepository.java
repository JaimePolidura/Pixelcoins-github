package es.serversurvival.pixelcoins.empresas._shared.accionistas.infrastructure;

import es.dependencyinjector.dependencies.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.delete.Delete;
import es.jaimetruman.select.Select;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.domain.AccionistaEmpresa;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.domain.AccionistaEmpresaRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class MySQLAccionistasEmpresasRepository extends DataBaseRepository<AccionistaEmpresa, UUID> implements AccionistaEmpresaRepository {
    private final static String FIELD_ID = "accionistaId";
    private final static String TABLE_NAME = "accionistas_empresas";

    protected MySQLAccionistasEmpresasRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(AccionistaEmpresa empresa) {
        super.save(empresa);
    }

    @Override
    public Optional<AccionistaEmpresa> findById(UUID accionistaId) {
        return super.findById(accionistaId);
    }

    @Override
    public List<AccionistaEmpresa> findByJugadorId(UUID accionisaJugadorId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("accionisaJugadorId").equal(accionisaJugadorId));
    }

    @Override
    public List<AccionistaEmpresa> findByEmpresaId(UUID empresaId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("empresaId").equal(empresaId));
    }

    @Override
    public Optional<AccionistaEmpresa> findByEmpresaIdAndJugadorId(UUID empresaId, UUID accionisaJugadorId) {
        return super.buildObjectFromQuery(Select.from(TABLE_NAME).where("empresaId")
                .equal(empresaId).and("accionisaJugadorId").equal(accionisaJugadorId));
    }

    @Override
    public List<AccionistaEmpresa> findAll() {
        return super.all();
    }

    @Override
    public void deleteByEmpresaId(UUID empresaId) {
        super.execute(Delete.from(TABLE_NAME).where("empresaId").equal(empresaId));
    }

    @Override
    public void deleteById(UUID accionistaId) {
        super.deleteById(accionistaId);
    }

    @Override
    protected EntityMapper<AccionistaEmpresa> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .idField(FIELD_ID).
                classesToMap(AccionistaEmpresa.class)
                .build();
    }

    @Override
    public AccionistaEmpresa buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new AccionistaEmpresa(
                UUID.fromString(rs.getString("accionistaId")),
                UUID.fromString(rs.getString("empresaId")),
                UUID.fromString(rs.getString("accionisaJugadorId")),
                rs.getInt("nAcciones")
        );
    }
}
