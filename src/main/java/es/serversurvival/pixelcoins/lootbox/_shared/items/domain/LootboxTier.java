package es.serversurvival.pixelcoins.lootbox._shared.items.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
public enum LootboxTier {
    RARO(10000, Material.DIAMOND),
    NORMAL(5000, Material.IRON_INGOT),
    COMUN(1000, Material.COAL);

    @Getter private final double precio;
    @Getter private final Material item;
}
