package es.serversurvival.empresas.accionistasempresasserver._shared.infrastructure;

import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.AccionEmpresaServer;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.AccionistasEmpresasServerRepository;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.TipoAccionista;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class MySQLAccionistasEmpresasServerRepository extends DataBaseRepository<AccionEmpresaServer, UUID>
        implements AccionistasEmpresasServerRepository {

    private static final String TABLE_NAME = "accionistasEmpresasServer";
    private static final String ID_FIELD_NAME = "accionEmpresaServerId";

    public MySQLAccionistasEmpresasServerRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(AccionEmpresaServer accionistaEmpresaServer) {
        super.save(accionistaEmpresaServer);
    }

    @Override
    public Optional<AccionEmpresaServer> findById(UUID id) {
        return super.findById(id);
    }

    @Override
    public List<AccionEmpresaServer> findByEmpresa(String empresa) {
        return buildListFromQuery(Select.from(TABLE_NAME).where("empresa").equal(empresa));
    }

    @Override
    public List<AccionEmpresaServer> findByNombreAccionista(String nombreAccionista) {
        return buildListFromQuery(Select.from(TABLE_NAME).where("nombreAccionista").equal(ID_FIELD_NAME));
    }

    @Override
    public void deleteById(UUID id) {
        super.deleteById(id);
    }

    @Override
    protected EntityMapper<AccionEmpresaServer> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .idField(ID_FIELD_NAME)
                .classToMap(AccionEmpresaServer.class)
                .build();
    }

    @Override
    public AccionEmpresaServer buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new AccionEmpresaServer(
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
