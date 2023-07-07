package es.serversurvival.pixelcoins.lootbox.items.domain;

import es.serversurvival._shared.items.ItemMinecraftEncantamientos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public final class LootboxItem {
    @Getter private UUID lootboxItemId;
    @Getter private LootboxTier tier;
    @Getter private LootboxItemRareza rareza;
    @Getter private String nombre;
    @Getter private int cantidadMinima;
    @Getter private int cantidadMaxima;
    @Getter private ItemMinecraftEncantamientos encantamientos;

    public int getCantidadAleatoria() {
        return (int) ((Math.random() * cantidadMaxima - cantidadMinima + 1) + cantidadMinima);
    }

    public boolean puedeTenerVariasCantidades() {
        return cantidadMinima != cantidadMaxima;
    }
}
