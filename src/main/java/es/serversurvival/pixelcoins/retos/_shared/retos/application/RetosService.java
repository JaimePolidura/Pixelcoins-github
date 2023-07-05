package es.serversurvival.pixelcoins.retos._shared.retos.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.ModuloReto;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.Reto;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.RetosRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RetosService {
    private final RetosRepository retosRepository;

    public List<Reto> findByRetoPadreProgresionIdSortByPosicion(UUID retoPadreProgresionId) {
        return retosRepository.findByRetoPadreProgresionIdSortByPosicion(retoPadreProgresionId);
    }

    public List<Reto> findByModuloAndRetoPadreId(ModuloReto modulo, UUID retoPadreId) {
        return this.retosRepository.findByModuloAndRetoPadreId(modulo, retoPadreId);
    }

    public Reto getById(UUID retoId) {
        return retosRepository.findById(retoId)
                .orElseThrow(() -> new ResourceNotFound(String.format("Reto con id %s no encontrado", retoId)));
    }

    public Reto getByMapping(RetoMapping mapping) {
        return retosRepository.findByMapping(mapping)
                .orElseThrow(() -> new ResourceNotFound(String.format("Reto con mapping %s no encontrado", mapping)));
    }
}
