package es.serversurvival.v1.empresas.accionistasserver._shared.infrastructure;

import es.dependencyinjector.dependencies.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;
import es.serversurvival.v1.empresas.accionistasserver._shared.domain.AccionistaServer;
import es.serversurvival.v1.empresas.accionistasserver._shared.domain.AccionistasServerRepository;
import es.serversurvival.v1.empresas.accionistasserver._shared.domain.TipoAccionista;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class MySQLAccionistasServerRepository extends DataBaseRepository<AccionistaServer, UUID>
        implements AccionistasServerRepository {

    private static final String TABLE_NAME = "accionistasServer";
    private static final String ID_FIELD_NAME = "accionistaServerId";

    public MySQLAccionistasServerRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(AccionistaServer accionistaEmpresaServer) {
        super.save(accionistaEmpresaServer);
    }

    @Override
    public Optional<AccionistaServer> findById(UUID id) {
        return super.findById(id);
    }

    @Override
    public List<AccionistaServer> findByEmpresa(String empresa) {
        return buildListFromQuery(Select.from(TABLE_NAME).where("empresa").equal(empresa));
    }

    @Override
    public List<AccionistaServer> findByNombreAccionista(String nombreAccionista) {
        return buildListFromQuery(Select.from(TABLE_NAME).where("nombreAccionista").equal(nombreAccionista));
    }

    @Override
    public void deleteById(UUID id) {
        super.deleteById(id);
    }

    @Override
    protected EntityMapper<AccionistaServer> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .idField(ID_FIELD_NAME)
                .classesToMap(AccionistaServer.class)
                .build();
    }

    @Override
    public AccionistaServer buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new AccionistaServer(
                UUID.fromString(rs.getString(ID_FIELD_NAME)),
                rs.getString("nombreAccionista"),
                TipoAccionista.valueOf(rs.getString("tipoAccionista")),
                rs.getString("empresa"),
                rs.getInt("cantidad"),
                rs.getDouble("precioApertura"),
                rs.getString("fechaApertura")
        );
    }
}
