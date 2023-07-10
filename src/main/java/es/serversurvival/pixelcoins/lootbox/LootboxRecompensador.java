package es.serversurvival.pixelcoins.lootbox;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.lootbox._shared.propiedad.application.LootboxEnPropiedadService;
import es.serversurvival.pixelcoins.lootbox._shared.propiedad.domain.LootboxEnPropiedad;
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
