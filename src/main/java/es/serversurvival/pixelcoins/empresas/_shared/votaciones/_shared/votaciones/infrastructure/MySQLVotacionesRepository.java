package es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.infrastructure;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.ResultsetObjetBuilder;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.TipoVotacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.Votacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.VotacionesRepository;
import es.serversurvival.pixelcoins.empresas.cambiardirector.CambiarDirectorVotacion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class MySQLVotacionesRepository extends DataBaseRepository<Votacion, UUID> implements VotacionesRepository {
    private static final String TABLE_NAME = "empresas_votaciones";
    private static final String FIELD_ID = "votacionId";

    private final DependenciesRepository dependenciesRepository;

    public MySQLVotacionesRepository(DatabaseConfiguration databaseConnection, DependenciesRepository dependenciesRepository) {
        super(databaseConnection);
        this.dependenciesRepository = dependenciesRepository;
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
        return super.all();
    }

    @Override
    protected EntityMapper<Votacion> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .classesToMap(CambiarDirectorVotacion.class)
                .idField(FIELD_ID)
                .build();
    }

    @Override
    public Votacion buildObjectFromResultSet(ResultSet rs) throws SQLException {
        TipoVotacion tipoVotacion = TipoVotacion.valueOf(rs.getString("tipo"));

        return (Votacion) dependenciesRepository.filterByImplementsInterfaceWithGeneric(ResultsetObjetBuilder.class,
                        tipoVotacion.getVotacionTypeClass())
                .orElseThrow(() -> new ResourceNotFound("ResultsetObjetBuilder no enontrado para " + tipoVotacion))
                .build(rs);
    }
}
