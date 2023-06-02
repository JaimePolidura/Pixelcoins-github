package es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class PosicionesService {
    private final PosicionesRepository repository;

    public void save(Posicion posicion) {
        repository.save(posicion);
    }

    public void savePosicionAbiertaConNuevaCantidad(Posicion posicion) {
        if(posicion.estaLaPosicionVacia()){
            deleteById(posicion.getPosicionId());
        }else{
            save(posicion);
        }
    }

    public Posicion getById(UUID posicionId) {
        return repository.findById(posicionId)
                .orElseThrow(() -> new ResourceNotFound("Posicion no encontrada"));
    }

    public void deleteById(UUID posicionId) {
        repository.deleteById(posicionId);
    }
}
