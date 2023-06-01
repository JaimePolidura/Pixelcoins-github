package es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones;

import es.dependencyinjector.dependencies.annotations.Service;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public final class PosicionesService {
    private final PosicionesRepository repository;

    public void save(Posicion posicion) {
        repository.save(posicion);
    }
}
