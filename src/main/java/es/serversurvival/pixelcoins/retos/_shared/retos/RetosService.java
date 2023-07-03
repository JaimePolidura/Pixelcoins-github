package es.serversurvival.pixelcoins.retos._shared.retos;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class RetosService {
    private final RetosRepository retosRepository;

    public List<Reto> findByRetoLineaPadre(UUID retoLineaPadreId) {
        return null;
    }

    public Reto getById(int retoId) {
        return retosRepository.findById(retoId)
                .orElseThrow(() -> new ResourceNotFound(String.format("Reto con id %s no encontrado", retoId)));
    }
}
