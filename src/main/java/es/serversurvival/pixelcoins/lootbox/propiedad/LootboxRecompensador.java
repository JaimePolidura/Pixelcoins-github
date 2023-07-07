package es.serversurvival.pixelcoins.lootbox.propiedad;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.application.utils.Utils;
import es.serversurvival.pixelcoins.lootbox.propiedad._shared.application.LootboxEnPropiedadService;
import es.serversurvival.pixelcoins.lootbox.propiedad._shared.domain.LootboxEnPropiedad;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.Reto;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.recompensas.RecompensadorReto;
import lombok.AllArgsConstructor;

import java.util.UUID;

import static es.jaime.javaddd.application.utils.Utils.*;

@Service
@AllArgsConstructor
public final class LootboxRecompensador implements RecompensadorReto {
    private final LootboxEnPropiedadService lootboxEnPropiedadService;

    @Override
    public void recompensar(UUID jugadorId, Reto reto) {
        repeat(reto.getNLootboxesRecompensa(), () -> {
            lootboxEnPropiedadService.save(LootboxEnPropiedad.builder()
                    .tier(reto.getLootboxTierRecompensa())
                    .jugadorId(jugadorId)
                    .build());
        });
    }
}
