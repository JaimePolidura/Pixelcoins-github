package es.serversurvival.pixelcoins.lootbox;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.Reto;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.recompensas.RecompensadorReto;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class LootboxRecompensador implements RecompensadorReto {
    @Override
    public void recompensar(UUID jugadorId, Reto reto) {

    }
}
