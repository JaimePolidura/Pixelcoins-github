package es.serversurvival.pixelcoins.lootbox._shared.items.domain;

import es.serversurvival._shared.items.ItemMinecraftEncantamientos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class LootboxItemSeleccionadaoResultado {
    @Getter private final UUID lootboxItemId;
    @Getter private final String nombre;
    @Getter private final int cantidad;
    @Getter private final ItemMinecraftEncantamientos encantamientos;
}
