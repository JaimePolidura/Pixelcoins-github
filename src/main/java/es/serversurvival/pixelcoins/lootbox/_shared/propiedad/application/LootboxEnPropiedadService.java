package es.serversurvival.pixelcoins.lootbox._shared.propiedad.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.pixelcoins.lootbox._shared.propiedad.domain.LootboxCompradaEstado;
import es.serversurvival.pixelcoins.lootbox._shared.propiedad.domain.LootboxEnPropiedad;
import es.serversurvival.pixelcoins.lootbox._shared.propiedad.domain.LootboxEnPropiedadRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class LootboxEnPropiedadService {
    private final LootboxEnPropiedadRepository repository;

    public void save(LootboxEnPropiedad lootboxEnPropiedad) {
        repository.save(lootboxEnPropiedad);
    }

    public LootboxEnPropiedad getById(UUID lootboxEnPropiedadId) {
        return repository.findById(lootboxEnPropiedadId)
                .orElseThrow(() -> new ResourceNotFound("Lootbox no encontrada"));
    }

    public List<LootboxEnPropiedad> findByJugadorAndEstado(UUID jugadorId, LootboxCompradaEstado estado) {
        return repository.findByJugadorAndEstado(jugadorId, estado);
    }
}
