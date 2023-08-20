package es.serversurvival.pixelcoins.lootbox._shared.propiedad.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.AlreadyExists;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins.config._shared.application.Configuration;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxTier;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class LootboxEnPropiedadValidator {
    private final LootboxEnPropiedadService lootboxEnPropiedadService;
    private final Configuration configuration;
    private final Validador validador;

    public void tienePixelcoins(UUID jugadorId, LootboxTier tier) {
        validador.jugadorTienePixelcoins(jugadorId, configuration.getDouble(tier.getConfigurationKey()));
    }

    public void tieneEnPropiedadLaLootbox(UUID jugadorId, UUID lootboxEnPropiedadId) {
        if(!lootboxEnPropiedadService.getById(lootboxEnPropiedadId).getJugadorId().equals(jugadorId)){
            throw new NotTheOwner("No tienes esa lootbox");
        }
    }

    public void lootboxPendienteDeAbrir(UUID lootboxEnPropiedadId) {
        if(!lootboxEnPropiedadService.getById(lootboxEnPropiedadId).pendienteDeAbrir()){
            throw new AlreadyExists("No se puede abrir el lootbox");
        }
    }
}
