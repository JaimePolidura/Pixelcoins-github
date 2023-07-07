package es.serversurvival.pixelcoins.lootbox.propiedad._shared.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LootboxEnPropiedadRepository {
    void save(LootboxEnPropiedad lootboxEnPropiedad);

    Optional<LootboxEnPropiedad> findById(UUID lootboxEnPropiedadId);

    List<LootboxEnPropiedad> findByJugadorAndEstado(UUID jugadorId, LootboxCompradaEstado estado);
}
