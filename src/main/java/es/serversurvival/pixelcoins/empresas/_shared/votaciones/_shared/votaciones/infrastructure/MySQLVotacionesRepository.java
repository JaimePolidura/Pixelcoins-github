package es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.infrastructure;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.ConditionalClassMapping;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.TipoVotacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.Votacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.VotacionesRepository;
import es.serversurvival.pixelcoins.empresas.cambiardirector.CambiarDirectorVotacion;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@MySQLRepository
public final class MySQLVotacionesRepository extends Repository<Votacion, UUID, TipoVotacion> implements VotacionesRepository {
    private static final String TABLE_NAME = "empresas_votaciones";
    private static final String FIELD_ID = "votacionId";

    public MySQLVotacionesRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void save(Votacion votacion) {
        super.save(votacion);
    }

    @Override
    public List<Votacion> findByEmpresaId(UUID empresaId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("empresaId").equal(empresaId));
    }

    @Override
    public Optional<Votacion> findById(UUID votacionId) {
        return super.findById(votacionId);
    }

    @Override
    public List<Votacion> findAll() {
        return super.findAll();
    }

    @Override
    public EntityMapper<Votacion, TipoVotacion> entityMapper() {
        return EntityMapper.builder()
                .classesToMap(Votacion.class)
                .idField(FIELD_ID)
                .table(TABLE_NAME)
                .conditionalClassMapping(ConditionalClassMapping.<Votacion, TipoVotacion>builder()
                        .entitiesTypeMapper(Map.of(TipoVotacion.CAMBIAR_DIRECTOR, CambiarDirectorVotacion.class))
                        .typeValueAccessor(resultSet -> TipoVotacion.valueOf(resultSet.getString("tipo")))
                        .typeClass(TipoVotacion.class)
                        .build())
                .build();
    }
}
