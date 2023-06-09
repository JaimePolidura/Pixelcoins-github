package es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.domain.Voto;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.domain.VotosRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class VotosService {
    private final VotosRepository repository;

    public void save(Voto voto) {
        repository.save(voto);
    }

    public int getNAccionesVotadas(UUID votacionId) {
        return this.repository.findByVotacionId(votacionId).stream()
                .mapToInt(Voto::getNAcciones)
                .sum();
    }

    public int getVotosContra(UUID votacionId) {
        return this.repository.findByVotacionId(votacionId).stream()
                .filter(voto -> !voto.isAfavor())
                .mapToInt(Voto::getNAcciones)
                .sum();
    }

    public int getVotosFavor(UUID votacionId) {
        return this.repository.findByVotacionId(votacionId).stream()
                .filter(Voto::isAfavor)
                .mapToInt(Voto::getNAcciones)
                .sum();
    }

    public boolean haVotado(UUID jugadorId, UUID votacionId) {
        return findByJugadorIdAndVotacionId(jugadorId, votacionId).isPresent();
    }

    public Optional<Voto> findByJugadorIdAndVotacionId(UUID jugadorId, UUID votacionId) {
        return repository.findByJugadorIdAndVotacionId(jugadorId, votacionId);
    }

    public List<Voto> findByVotacionId(UUID votacionId) {
        return repository.findByVotacionId(votacionId);
    }

    public Voto getById(UUID votoId) {
        return repository.findById(votoId).orElseThrow(() -> new ResourceNotFound("Voto no encontrado"));
    }

    public void deleteById(UUID votoId) {
        this.repository.deleteById(votoId);
    }
}
