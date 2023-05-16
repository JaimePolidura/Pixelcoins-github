package es.serversurvival.v2.pixelcoins.deudas._shared;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class DeudasService {
    private final DeudasRepository deudasRepository;

    public void save(Deuda deuda) {
        this.deudasRepository.save(deuda);
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
