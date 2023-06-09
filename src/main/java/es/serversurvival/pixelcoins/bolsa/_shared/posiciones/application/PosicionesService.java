package es.serversurvival.pixelcoins.bolsa._shared.posiciones.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.Posicion;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.PosicionesRepository;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.TipoPosicion;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public final class PosicionesService {
    private final PosicionesRepository repository;

    public void save(Posicion posicion) {
        repository.save(posicion);
    }

    public List<Posicion> findPosicionesCerradasSortByRentabilidad() {
        return repository.findPosicionesCerradasSortByRentabilidad();
    }

    public void savePosicionAbiertaConNuevaCantidad(Posicion posicion) {
        if(posicion.estaLaPosicionVacia()){
            deleteById(posicion.getPosicionId());
        }else{
            save(posicion);
        }
    }

    public List<Posicion> findPosicionesCerradasByJugadorId(UUID jugadorId) {
        return repository.findByJugadorId(jugadorId).stream()
                .filter(posicion -> posicion.getTipoPosicion() == TipoPosicion.CERRADO)
                .collect(Collectors.toList());
    }

    public List<Posicion> findPosicionesAbiertasByJugadorId(UUID jugadorId) {
        return repository.findByJugadorId(jugadorId).stream()
                .filter(posicion -> posicion.getTipoPosicion() == TipoPosicion.ABIERTO)
                .collect(Collectors.toList());
    }

    public Posicion getById(UUID posicionId) {
        return repository.findById(posicionId)
                .orElseThrow(() -> new ResourceNotFound("Posicion no encontrada"));
    }

    public void deleteById(UUID posicionId) {
        repository.deleteById(posicionId);
    }
}
