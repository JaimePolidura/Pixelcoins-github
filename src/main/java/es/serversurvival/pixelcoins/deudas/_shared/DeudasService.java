package es.serversurvival.pixelcoins.deudas._shared;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public final class DeudasService {
    private final DeudasRepository deudasRepository;

    public void save(Deuda deuda) {
        this.deudasRepository.save(deuda);
    }

    public List<Deuda> findByAcredorJugadorIdPendiente(UUID acredorJugadorId) {
        return deudasRepository.findByAcredorJugadorId(acredorJugadorId).stream()
                .filter(Deuda::estaPendiente)
                .collect(Collectors.toList());
    }

    public List<Deuda> findByJugadorIdPendiente(UUID jugadorId) {
        return deudasRepository.findByJugadorId(jugadorId).stream()
                .filter(Deuda::estaPendiente)
                .collect(Collectors.toList());
    }

    public List<Deuda> findByDeudorJugadorIdPendiente(UUID deudorJugadorId) {
        return deudasRepository.findByDeudorJugadorId(deudorJugadorId).stream()
                .filter(Deuda::estaPendiente)
                .collect(Collectors.toList());
    }

    public Deuda getById(UUID deudaId) {
        return this.deudasRepository.findById(deudaId).orElseThrow(() -> new ResourceNotFound("Deuda no encontrada"));
    }

    public List<Deuda> findAll() {
        return this.deudasRepository.findAll();
    }

    public void deleteById(UUID id) {
        this.deudasRepository.deleteById(id);
    }
}
