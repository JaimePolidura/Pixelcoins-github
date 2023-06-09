package es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.infrastructure;

import es.dependencyinjector.dependencies.annotations.Repository;
import es.jaime.configuration.DatabaseConfiguration;
import es.jaime.mapper.EntityMapper;
import es.jaime.repository.DataBaseRepository;
import es.jaimetruman.select.Select;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.domain.Voto;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.domain.VotosRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public final class MySQLVotoRepository extends DataBaseRepository<Voto, UUID> implements VotosRepository {
    public static final String TABLE_NAME = "votos";
    public static final String FIELD_ID = "votoId";

    protected MySQLVotoRepository(DatabaseConfiguration databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public void save(Voto voto) {
        super.save(voto);
    }

    @Override
    public List<Voto> findByVotacionId(UUID votacionId) {
        return super.buildListFromQuery(Select.from(TABLE_NAME).where("votacionId").equal(votacionId));
    }

    @Override
    public Optional<Voto> findById(UUID votoId) {
        return super.findById(votoId);
    }

    @Override
    public Optional<Voto> findByJugadorIdAndVotacionId(UUID jugadorId, UUID votacionId) {
        return super.buildObjectFromQuery(Select.from(TABLE_NAME)
                .where("jugadorId").equal(jugadorId)
                .and("votacionId").equal(votacionId));
    }

    @Override
    public void deleteById(UUID votoId) {
        super.deleteById(votoId);
    }

    @Override
    protected EntityMapper<Voto> entityMapper() {
        return EntityMapper.table(TABLE_NAME)
                .classesToMap(Voto.class)
                .idField(FIELD_ID)
                .build();
    }

    @Override
    public Voto buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Voto(
                UUID.fromString(rs.getString("votoId")),
                UUID.fromString(rs.getString("votacionId")),
                UUID.fromString(rs.getString("jugadorId")),
                rs.getBoolean("afavor"),
                rs.getInt("nAcciones"),
                rs.getTimestamp("fechaVotacion").toLocalDateTime()
        );
    }
}
