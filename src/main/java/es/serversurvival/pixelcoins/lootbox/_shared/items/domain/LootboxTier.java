package es.serversurvival.pixelcoins.lootbox._shared.items.domain;

import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
public enum LootboxTier {
    RARO(ConfigurationKey.LOOTBOX_PRECIO_RARO, Material.DIAMOND),
    NORMAL(ConfigurationKey.LOOTBOX_PRECIO_NORMAL, Material.IRON_INGOT),
    COMUN(ConfigurationKey.LOOTBOX_PRECIO_COMUN, Material.COAL);

    @Getter private final ConfigurationKey configurationKey;
    @Getter private final Material item;
}
