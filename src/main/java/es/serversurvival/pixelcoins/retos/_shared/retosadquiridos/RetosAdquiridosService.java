package es.serversurvival.pixelcoins.retos._shared.retosadquiridos;

import es.dependencyinjector.dependencies.annotations.Service;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class RetosAdquiridosService {
    public void save(RetoAdquirido retoAdquirido) {

    }

    public boolean estaAdquirido(UUID jugadorId, int retoId) {
        return false;
    }
}
