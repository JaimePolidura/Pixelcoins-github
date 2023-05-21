package es.serversurvival.v2.pixelcoins.mensajes._shared.mensajes;

import es.dependencyinjector.dependencies.annotations.Service;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class MensajesService {
    private final MensajesRepository repository;

    public void save(Mensaje mensaje) {
        repository.save(mensaje);
    }

    public List<Mensaje> findByJugadorIdNoVistos(UUID jugadorId) {
        return repository.findByJugadorIdNoVistos(jugadorId);
    }

    public void deleteByFechaVistaLessThan(LocalDateTime value){
        repository.deleteByFechaVistaLessThan(value);
    }
}
