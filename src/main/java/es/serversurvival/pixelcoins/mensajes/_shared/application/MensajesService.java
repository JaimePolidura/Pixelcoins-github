package es.serversurvival.pixelcoins.mensajes._shared.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.mensajes._shared.domain.Mensaje;
import es.serversurvival.pixelcoins.mensajes._shared.domain.MensajesRepository;
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

    public void deleteByFechaVistoLessThan(LocalDateTime value){
        repository.deleteByFechaVistaLessThan(value);
    }
}
