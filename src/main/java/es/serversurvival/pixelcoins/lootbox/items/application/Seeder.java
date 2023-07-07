package es.serversurvival.pixelcoins.lootbox.items.application;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival._shared.eventospixelcoins.PluginIniciado;
import es.serversurvival._shared.items.ItemMinecraftEncantamientos;
import es.serversurvival.pixelcoins.lootbox.items.domain.LootboxItem;
import es.serversurvival.pixelcoins.lootbox.items.domain.LootboxItemRareza;
import es.serversurvival.pixelcoins.lootbox.items.domain.LootboxTier;
import lombok.AllArgsConstructor;
import org.bukkit.Material;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static es.serversurvival.pixelcoins.lootbox.items.domain.LootboxTier.*;

@EventHandler
@AllArgsConstructor
public final class Seeder {
    private final LootboxItemsService lootboxItemsService;

    @EventListener
    public void on(PluginIniciado e) {
        List<LootboxItem> lootboxComun = List.of(
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.OAK_LOG, 16, 64),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.BIRCH_LOG, 16, 64),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.ACACIA_LOG, 16, 64),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.JUNGLE_LOG, 16, 64),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.DARK_OAK_LOG, 16, 64),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.SPRUCE_LOG, 16, 64),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.OAK_DOOR, 2, 32),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.JUNGLE_DOOR, 2, 32),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.DARK_OAK_DOOR, 3, 32),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.SAND, 32, 64),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.OAK_FENCE, 2, 43),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.JUNGLE_FENCE, 2, 43),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.BIRCH_FENCE_GATE, 2, 43),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.OAK_FENCE_GATE, 2, 43),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.TORCH, 32, 64),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.IRON_PICKAXE, 1, 1),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.IRON_SWORD, 1, 1),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.IRON_AXE, 1, 1),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.IRON_SHOVEL, 1, 1),

                item(COMUN, LootboxItemRareza.COMUN, Material.SANDSTONE, 32, 64),
                item(COMUN, LootboxItemRareza.COMUN, Material.STONE_BRICKS, 32, 64),
                item(COMUN, LootboxItemRareza.COMUN, Material.RED_SANDSTONE, 32, 64),
                item(COMUN, LootboxItemRareza.COMUN, Material.CHEST, 2, 16),
                item(COMUN, LootboxItemRareza.COMUN, Material.GLASS, 16, 32),
                item(COMUN, LootboxItemRareza.COMUN, Material.WHITE_BANNER, 1, 16),
                item(COMUN, LootboxItemRareza.COMUN, Material.GLOWSTONE, 1, 16),
                item(COMUN, LootboxItemRareza.COMUN, Material.CAMPFIRE, 16, 32),
                item(COMUN, LootboxItemRareza.COMUN, Material.CAULDRON, 1, 16),
                item(COMUN, LootboxItemRareza.COMUN, Material.ITEM_FRAME, 1, 4),
                item(COMUN, LootboxItemRareza.COMUN, Material.REDSTONE_BLOCK, 1, 32),
                item(COMUN, LootboxItemRareza.COMUN, Material.OBSERVER, 4, 8),
                item(COMUN, LootboxItemRareza.COMUN, Material.REPEATER, 4, 8),
                item(COMUN, LootboxItemRareza.COMUN, Material.HOPPER, 4, 8),
                item(COMUN, LootboxItemRareza.COMUN, Material.BUCKET, 1, 16),
                item(COMUN, LootboxItemRareza.COMUN, Material.NAME_TAG, 2, 2),
                item(COMUN, LootboxItemRareza.COMUN, Material.IRON_HELMET, 1, 1),
                item(COMUN, LootboxItemRareza.COMUN, Material.IRON_CHESTPLATE, 1, 1),
                item(COMUN, LootboxItemRareza.COMUN, Material.IRON_LEGGINGS, 1, 1),
                item(COMUN, LootboxItemRareza.COMUN, Material.IRON_BOOTS, 1, 1),
                item(COMUN, LootboxItemRareza.COMUN, Material.COOKED_BEEF, 16, 64),
                item(COMUN, LootboxItemRareza.COMUN, Material.COOKED_MUTTON, 16, 64),
                item(COMUN, LootboxItemRareza.COMUN, Material.COOKED_PORKCHOP, 16, 64),
                item(COMUN, LootboxItemRareza.COMUN, Material.POTATO, 16, 64),
                item(COMUN, LootboxItemRareza.COMUN, Material.BOOK, 4, 8),
                item(COMUN, LootboxItemRareza.COMUN, Material.IRON_BARS, 16, 32),
                item(COMUN, LootboxItemRareza.COMUN, Material.MAGENTA_DYE, 16, 32),
                item(COMUN, LootboxItemRareza.COMUN, Material.PURPLE_DYE, 16, 32),
                item(COMUN, LootboxItemRareza.COMUN, Material.YELLOW_DYE, 16, 32),
                item(COMUN, LootboxItemRareza.COMUN, Material.GREEN_DYE, 16, 32),
                item(COMUN, LootboxItemRareza.COMUN, Material.LIME_DYE, 16, 32),
                item(COMUN, LootboxItemRareza.COMUN, Material.LIGHT_GRAY_DYE, 16, 32),
                item(COMUN, LootboxItemRareza.COMUN, Material.RED_DYE, 16, 32),
                item(COMUN, LootboxItemRareza.COMUN, Material.WHITE_DYE, 16, 32),
                item(COMUN, LootboxItemRareza.COMUN, Material.BLUE_DYE, 16, 32),
                item(COMUN, LootboxItemRareza.COMUN, Material.BLACK_DYE, 16, 32),
                item(COMUN, LootboxItemRareza.COMUN, Material.BROWN_DYE, 16, 32),
                item(COMUN, LootboxItemRareza.COMUN, Material.CYAN_DYE, 16, 32),

                item(COMUN, LootboxItemRareza.RARO, Material.QUARTZ_BLOCK, 16, 32),
                item(COMUN, LootboxItemRareza.RARO, Material.LAPIS_ORE, 16, 32),
                item(COMUN, LootboxItemRareza.RARO, Material.CHEST, 16, 32),
                item(COMUN, LootboxItemRareza.RARO, Material.GLASS, 32, 64),
                item(COMUN, LootboxItemRareza.RARO, Material.GLOWSTONE, 16, 32),
                item(COMUN, LootboxItemRareza.RARO, Material.LANTERN, 16, 32),
                item(COMUN, LootboxItemRareza.RARO, Material.ANVIL, 1, 4),
                item(COMUN, LootboxItemRareza.RARO, Material.CAMPFIRE, 32, 64),
                item(COMUN, LootboxItemRareza.RARO, Material.ITEM_FRAME, 4, 16),
                item(COMUN, LootboxItemRareza.RARO, Material.BOOKSHELF, 1, 4),
                item(COMUN, LootboxItemRareza.RARO, Material.GRINDSTONE, 1, 1),
                item(COMUN, LootboxItemRareza.RARO, Material.ENDER_PEARL, 1, 4),
                item(COMUN, LootboxItemRareza.RARO, Material.STICKY_PISTON, 1, 4),
                item(COMUN, LootboxItemRareza.RARO, Material.OBSERVER, 8, 16),
                item(COMUN, LootboxItemRareza.RARO, Material.REPEATER, 8, 16),
                item(COMUN, LootboxItemRareza.RARO, Material.HOPPER, 8, 16),
                item(COMUN, LootboxItemRareza.RARO, Material.SLIME_BLOCK, 1, 4),
                item(COMUN, LootboxItemRareza.RARO, Material.NAME_TAG, 2, 4),
                item(COMUN, LootboxItemRareza.RARO, Material.POWERED_RAIL, 1, 32),
                item(COMUN, LootboxItemRareza.RARO, Material.EMERALD, 1, 8),
                item(COMUN, LootboxItemRareza.RARO, Material.BOOK, 8, 16),
                item(COMUN, LootboxItemRareza.RARO, Material.BLAZE_ROD, 8, 16),
                item(COMUN, LootboxItemRareza.RARO, Material.NETHER_WART, 8, 16),
                item(COMUN, LootboxItemRareza.RARO, Material.CANDLE, 4, 8),

                item(COMUN, LootboxItemRareza.MUY_RARO, Material.ENCHANTING_TABLE, 1, 2),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.QUARTZ_BLOCK, 32, 64),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.LAPIS_ORE, 32, 64),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.CHEST, 32, 64),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.ANVIL, 4, 16),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.ITEM_FRAME, 4, 16),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.BOOKSHELF, 4, 16),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.ENDER_EYE, 1, 3),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.ENDER_PEARL, 4, 8),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.STICKY_PISTON, 4, 8),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.REPEATER, 16, 32),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.HOPPER, 16, 32),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.SLIME_BLOCK, 4, 8),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.DIAMOND_PICKAXE, 1, 1),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.DIAMOND_SWORD, 1, 1),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.DIAMOND_AXE, 1, 1),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.DIAMOND_SHOVEL, 1, 1),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.NAME_TAG, 4, 8)
        );
    }

    private LootboxItem item(LootboxTier tier, LootboxItemRareza rareza, Material material) {
        return new LootboxItem(UUID.randomUUID(), tier, rareza, material.name(), 1, 1, new ItemMinecraftEncantamientos(new LinkedList<>()));
    }

    private LootboxItem item(LootboxTier tier, LootboxItemRareza rareza, Material material, int cantidadMinima, int cantidadMaxima) {
        return new LootboxItem(UUID.randomUUID(), tier, rareza, material.name(), cantidadMinima, cantidadMaxima, new ItemMinecraftEncantamientos(new LinkedList<>()));
    }

    private LootboxItem item(LootboxTier tier, LootboxItemRareza rareza, Material material, ItemMinecraftEncantamientos encantamientos) {
        return new LootboxItem(UUID.randomUUID(), tier, rareza, material.name(), 1, 1, encantamientos);
    }

    private LootboxItem item(LootboxTier tier, LootboxItemRareza rareza, Material material, int cantidadMinima, int cantidadMaxima, ItemMinecraftEncantamientos encantamientos) {
        return new LootboxItem(UUID.randomUUID(), tier, rareza, material.name(), cantidadMinima, cantidadMaxima, encantamientos);
    }
}
