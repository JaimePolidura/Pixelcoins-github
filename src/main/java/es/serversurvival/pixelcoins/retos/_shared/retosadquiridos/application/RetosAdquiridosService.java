package es.serversurvival.pixelcoins.retos._shared.retosadquiridos.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.retos._shared.retosadquiridos.domain.RetoAdquirido;
import es.serversurvival.pixelcoins.retos._shared.retosadquiridos.domain.RetosAdquiridosRepository;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RetosAdquiridosService {
    private final RetosAdquiridosRepository repository;

    public void save(RetoAdquirido retoAdquirido) {
        repository.save(retoAdquirido);
    }

    public boolean estaAdquirido(UUID jugadorId, UUID retoId) {
        return repository.findByJugadorIdAndRetoId(jugadorId, retoId).isPresent();
    }
}
