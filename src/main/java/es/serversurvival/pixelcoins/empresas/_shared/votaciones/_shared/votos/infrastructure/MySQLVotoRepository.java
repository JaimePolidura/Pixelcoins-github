package es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.infrastructure;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.domain.Voto;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.domain.VotosRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MySQLRepository
public final class MySQLVotoRepository extends Repository<Voto, UUID, Object> implements VotosRepository {
    public static final String TABLE_NAME = "empresas_votos";
    public static final String FIELD_ID = "votoId";

    public MySQLVotoRepository(ConnectionManager connectionManager) {
        super(connectionManager);
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
    public EntityMapper<Voto, Object> entityMapper() {
        return EntityMapper.builder()
                .idField(FIELD_ID)
                .table(TABLE_NAME)
                .classesToMap(Voto.class)
                .build();
    }
}
