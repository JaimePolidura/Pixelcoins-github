package es.serversurvival.v2.pixelcoins.empresas._shared.votaciones._shared.votos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VotosRepository {
    void save(Voto voto);

    List<Voto> findByVotacionId(UUID votacionId);

    Optional<Voto> findById(UUID votoId);

    Optional<Voto> findByJugadorIdAndVotacionId(UUID jugadorId, UUID votacionId);

    void deleteById(UUID votoId);
}
