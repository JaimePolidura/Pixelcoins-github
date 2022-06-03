package es.serversurvival.empresas.ofertasaccionesserver._shared.infrastructure;

import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertasAccionesServerRepository;
import es.serversurvival.empresas.accionistasserver._shared.domain.TipoAccionista;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class MySQLOfertasAccionesServerRepository extends DataBaseRepository<OfertaAccionServer, UUID> implements OfertasAccionesServerRepository {
    private static final String TABLE_NAME = "ofertasAccionesServer";
    private static final String ID_FIELD_NAME = "ofertaAccionServerId";

    public MySQLOfertasAccionesServerRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(OfertaAccionServer ofertaMercadoServer) {
        super.save(ofertaMercadoServer);
    }

    @Override
    public Optional<OfertaAccionServer> findById(UUID id) {
        return super.findById(id);
    }

    @Override
    public List<OfertaAccionServer> findAll() {
        return super.all();
    }

    @Override
    public List<OfertaAccionServer> findByEmpresa(String empresa) {
        return buildListFromQuery(Select.from(TABLE_NAME).where("empresa").equal(empresa));
    }

    @Override
    public List<OfertaAccionServer> findByOfertanteNombre(String nombreOfertante) {
        return buildListFromQuery(Select.from(TABLE_NAME).where("nombreOfertante").equal(nombreOfertante));
    }

    @Override
    public void deleteById(UUID id) {
        super.deleteById(id);
    }

    @Override
    protected EntityMapper<OfertaAccionServer> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .classToMap(OfertaAccionServer.class)
                .idField(ID_FIELD_NAME)
                .build();
    }

    @Override
    public OfertaAccionServer buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new OfertaAccionServer(
                UUID.fromString(rs.getString(ID_FIELD_NAME)),
                rs.getString("nombreOfertante"),
                rs.getString("empresa"),
                rs.getDouble("precio"),
                rs.getInt("cantidad"),
                rs.getString("fecha"),
                TipoAccionista.valueOf(rs.getString("tipoOfertante")),
                rs.getDouble("precioApertura"),
                UUID.fromString(rs.getString("accionistaEmpresaServerId"))
        );
    }
}
