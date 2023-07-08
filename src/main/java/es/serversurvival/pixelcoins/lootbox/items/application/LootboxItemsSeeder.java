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
import org.bukkit.enchantments.Enchantment;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static es.serversurvival.pixelcoins.lootbox.items.domain.LootboxTier.*;

@EventHandler
@AllArgsConstructor
public final class LootboxItemsSeeder {
    private final LootboxItemsService lootboxItemsService;

    @EventListener
    public void on(PluginIniciado e) {
        if(lootboxItemsService.findAll().size() > 0){
            return;
        }

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
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.IRON_PICKAXE),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.IRON_SWORD),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.IRON_AXE),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.IRON_SHOVEL),

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
                item(COMUN, LootboxItemRareza.COMUN, Material.IRON_HELMET),
                item(COMUN, LootboxItemRareza.COMUN, Material.IRON_CHESTPLATE),
                item(COMUN, LootboxItemRareza.COMUN, Material.IRON_LEGGINGS),
                item(COMUN, LootboxItemRareza.COMUN, Material.IRON_BOOTS),
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
                item(COMUN, LootboxItemRareza.COMUN, Material.OBSIDIAN, 8, 16),
                item(COMUN, LootboxItemRareza.COMUN, Material.BREWING_STAND, 2, 4),
                item(COMUN, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.KNOCKBACK, 1)),

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
                item(COMUN, LootboxItemRareza.RARO, Material.GRINDSTONE),
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
                item(COMUN, LootboxItemRareza.RARO, Material.DIAMOND, 2, 16),
                item(COMUN, LootboxItemRareza.RARO, Material.IRON_BLOCK, 8, 16),
                item(COMUN, LootboxItemRareza.RARO, Material.SPYGLASS),
                item(COMUN, LootboxItemRareza.RARO, Material.LEAD, 2, 4),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.FIRE_ASPECT, 1)),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 2)),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.DURABILITY, 1)),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.DAMAGE_ALL, 2)),
                item(COMUN, LootboxItemRareza.MUY_COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.ARROW_DAMAGE, 2)),
                item(COMUN, LootboxItemRareza.RARO, Material.IRON_HORSE_ARMOR),
                item(COMUN, LootboxItemRareza.RARO, Material.SADDLE),
                item(COMUN, LootboxItemRareza.RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.FIRE_ASPECT, 1)),
                item(COMUN, LootboxItemRareza.RARO, Material.IRON_SHOVEL, Map.of(
                        Enchantment.DURABILITY, 2,
                        Enchantment.DIG_SPEED, 2
                )),
                item(COMUN, LootboxItemRareza.RARO, Material.IRON_PICKAXE, Map.of(
                        Enchantment.DURABILITY, 2,
                        Enchantment.DIG_SPEED, 2
                )),
                item(COMUN, LootboxItemRareza.RARO, Material.IRON_SWORD, Map.of(
                        Enchantment.DAMAGE_ALL, 3
                )),

                item(COMUN, LootboxItemRareza.MUY_RARO, Material.ENCHANTING_TABLE, 1, 2),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.QUARTZ_BLOCK, 32, 64),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.LAPIS_ORE, 32, 64),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.CHEST, 32, 64),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.ANVIL, 4, 16),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.BOOKSHELF, 4, 16),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.ENDER_EYE, 1, 3),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.ENDER_PEARL, 4, 8),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.STICKY_PISTON, 4, 8),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.REPEATER, 16, 32),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.HOPPER, 16, 32),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.SLIME_BLOCK, 4, 8),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.DIAMOND_PICKAXE),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.DIAMOND_SWORD),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.DIAMOND_AXE),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.DIAMOND_SHOVEL),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.NAME_TAG, 4, 8),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.DIAMOND_BLOCK, 2, 4),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.BEACON),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.IRON_BLOCK, 16, 32),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.ENDER_CHEST),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.IRON_PICKAXE, Map.of(
                        Enchantment.SILK_TOUCH, 1,
                        Enchantment.DURABILITY, 2,
                        Enchantment.DIG_SPEED, 2
                )),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.IRON_PICKAXE, Map.of(
                        Enchantment.DURABILITY, 2,
                        Enchantment.DIG_SPEED, 3,
                        Enchantment.LOOT_BONUS_BLOCKS, 2
                )),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.DIAMOND_PICKAXE, Map.of(
                        Enchantment.DURABILITY, 1,
                        Enchantment.DIG_SPEED, 2,
                        Enchantment.LOOT_BONUS_BLOCKS, 1
                )),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.DIAMOND_SWORD, Map.of(
                        Enchantment.DURABILITY, 2,
                        Enchantment.FIRE_ASPECT, 1,
                        Enchantment.LOOT_BONUS_MOBS, 1,
                        Enchantment.DAMAGE_ALL, 3
                )),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.IRON_SWORD, Map.of(
                        Enchantment.LOOT_BONUS_MOBS, 2,
                        Enchantment.DAMAGE_ALL, 1
                )),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.DIAMOND_SWORD, Map.of(
                        Enchantment.LOOT_BONUS_MOBS, 2,
                        Enchantment.DAMAGE_ALL, 1
                )),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.DIAMOND_CHESTPLATE, Map.of(
                        Enchantment.PROTECTION_ENVIRONMENTAL, 1
                )),
                item(COMUN, LootboxItemRareza.MUY_RARO, Material.IRON_HELMET, Map.of(
                        Enchantment.PROTECTION_ENVIRONMENTAL, 3
                ))
        );

        List<LootboxItem> lootboxNormal = List.of(
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.OAK_LOG, 48, 64),
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.SEA_LANTERN, 48, 64),
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.EMERALD, 8, 32),
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.EMERALD_BLOCK, 1, 16),
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.LANTERN, 32, 64),
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.IRON_BLOCK, 16, 32),
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.ENCHANTING_TABLE, 2, 4),
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.ITEM_FRAME, 16, 32),
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.SCAFFOLDING, 64, 64),
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.CHEST, 32, 48),
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.LEAD, 2, 8),
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.NAME_TAG, 4, 8),
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.BOOK, 8, 32),
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.EMERALD, 16, 32),
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.FIRE_ASPECT, 1)),
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 2)),
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.DURABILITY, 1)),
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.DAMAGE_ALL, 2)),
                item(NORMAL, LootboxItemRareza.MUY_COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.ARROW_DAMAGE, 2)),

                item(NORMAL, LootboxItemRareza.COMUN, Material.EMERALD, 16, 48),
                item(NORMAL, LootboxItemRareza.COMUN, Material.EMERALD_BLOCK, 1, 32),
                item(NORMAL, LootboxItemRareza.COMUN, Material.DIAMOND, 8, 16),
                item(NORMAL, LootboxItemRareza.COMUN, Material.LAPIS_BLOCK, 4, 8),
                item(NORMAL, LootboxItemRareza.COMUN, Material.NETHERITE_SCRAP, 3, 6),
                item(NORMAL, LootboxItemRareza.COMUN, Material.NETHERITE_INGOT, 1, 3),
                item(NORMAL, LootboxItemRareza.COMUN, Material.OBSIDIAN, 16, 32),
                item(NORMAL, LootboxItemRareza.COMUN, Material.GLOWSTONE, 48, 64),
                item(NORMAL, LootboxItemRareza.COMUN, Material.BOOKSHELF, 16, 32),
                item(NORMAL, LootboxItemRareza.COMUN, Material.ANVIL, 32, 64),
                item(NORMAL, LootboxItemRareza.COMUN, Material.IRON_BLOCK, 32, 48),
                item(NORMAL, LootboxItemRareza.COMUN, Material.ENCHANTING_TABLE, 4, 8),
                item(NORMAL, LootboxItemRareza.COMUN, Material.ITEM_FRAME, 32, 48),
                item(NORMAL, LootboxItemRareza.COMUN, Material.CHEST, 64, 64),
                item(NORMAL, LootboxItemRareza.COMUN, Material.HOPPER, 16, 32),
                item(NORMAL, LootboxItemRareza.COMUN, Material.OBSERVER, 16, 32),
                item(NORMAL, LootboxItemRareza.COMUN, Material.STICKY_PISTON, 8, 16),
                item(NORMAL, LootboxItemRareza.COMUN, Material.REPEATER, 32, 64),
                item(NORMAL, LootboxItemRareza.COMUN, Material.ARMOR_STAND, 1, 16),
                item(NORMAL, LootboxItemRareza.COMUN, Material.POWERED_RAIL, 16, 32),
                item(NORMAL, LootboxItemRareza.COMUN, Material.DIAMOND_PICKAXE),
                item(NORMAL, LootboxItemRareza.COMUN, Material.DIAMOND_SWORD),
                item(NORMAL, LootboxItemRareza.COMUN, Material.DIAMOND_AXE),
                item(NORMAL, LootboxItemRareza.COMUN, Material.DIAMOND_SHOVEL),
                item(NORMAL, LootboxItemRareza.COMUN, Material.NAME_TAG, 8, 16),
                item(NORMAL, LootboxItemRareza.COMUN, Material.FIREWORK_ROCKET, 32, 64),
                item(NORMAL, LootboxItemRareza.COMUN, Material.ENDER_CHEST),
                item(NORMAL, LootboxItemRareza.COMUN, Material.DIAMOND_HORSE_ARMOR),
                item(NORMAL, LootboxItemRareza.COMUN, Material.ENCHANTED_GOLDEN_APPLE, 1, 2),
                item(NORMAL, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.ARROW_KNOCKBACK, 2)),
                item(NORMAL, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 3)),
                item(NORMAL, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.FIRE_ASPECT, 2)),
                item(NORMAL, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.DURABILITY, 2)),
                item(NORMAL, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.DAMAGE_ALL, 3)),
                item(NORMAL, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.ARROW_DAMAGE, 3)),
                item(NORMAL, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.LOOT_BONUS_MOBS, 1)),
                item(NORMAL, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.LOOT_BONUS_BLOCKS, 1)),

                item(NORMAL, LootboxItemRareza.RARO, Material.LAPIS_BLOCK, 8, 16),
                item(NORMAL, LootboxItemRareza.RARO, Material.QUARTZ_BLOCK, 32, 64),
                item(NORMAL, LootboxItemRareza.RARO, Material.GOLD_BLOCK, 16, 32),
                item(NORMAL, LootboxItemRareza.RARO, Material.DIAMOND, 16, 32),
                item(NORMAL, LootboxItemRareza.RARO, Material.NETHERITE_SCRAP, 6, 18),
                item(NORMAL, LootboxItemRareza.RARO, Material.NETHERITE_INGOT, 3, 6),
                item(NORMAL, LootboxItemRareza.RARO, Material.SHULKER_BOX),
                item(NORMAL, LootboxItemRareza.RARO, Material.OBSIDIAN, 32, 64),
                item(NORMAL, LootboxItemRareza.RARO, Material.DIAMOND_ORE, 8, 16),
                item(NORMAL, LootboxItemRareza.RARO, Material.ANCIENT_DEBRIS, 16, 32),
                item(NORMAL, LootboxItemRareza.RARO, Material.WITHER_ROSE, 4, 8),
                item(NORMAL, LootboxItemRareza.RARO, Material.BOOKSHELF, 32, 48),
                item(NORMAL, LootboxItemRareza.RARO, Material.ENDER_EYE, 4, 12),
                item(NORMAL, LootboxItemRareza.RARO, Material.WITHER_SKELETON_SKULL, 1, 2),
                item(NORMAL, LootboxItemRareza.RARO, Material.SLIME_BLOCK, 8, 16),
                item(NORMAL, LootboxItemRareza.RARO, Material.HOPPER, 32, 64),
                item(NORMAL, LootboxItemRareza.RARO, Material.OBSERVER, 32, 48),
                item(NORMAL, LootboxItemRareza.RARO, Material.STICKY_PISTON, 16, 32),
                item(NORMAL, LootboxItemRareza.RARO, Material.BEACON, 2, 2),
                item(NORMAL, LootboxItemRareza.RARO, Material.ENDER_CHEST, 2, 4),
                item(NORMAL, LootboxItemRareza.RARO, Material.DIAMOND_HELMET),
                item(NORMAL, LootboxItemRareza.RARO, Material.DIAMOND_CHESTPLATE),
                item(NORMAL, LootboxItemRareza.RARO, Material.DIAMOND_LEGGINGS),
                item(NORMAL, LootboxItemRareza.RARO, Material.DIAMOND_BOOTS),
                item(NORMAL, LootboxItemRareza.RARO, Material.ENCHANTED_GOLDEN_APPLE, 1, 4),
                item(NORMAL, LootboxItemRareza.RARO, Material.HEART_OF_THE_SEA, 1, 4),
                item(NORMAL, LootboxItemRareza.RARO, Material.NAUTILUS_SHELL, 1, 4),
                item(NORMAL, LootboxItemRareza.RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.ARROW_FIRE, 1)),
                item(NORMAL, LootboxItemRareza.RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.ARROW_INFINITE, 1)),
                item(NORMAL, LootboxItemRareza.RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.DURABILITY, 2)),
                item(NORMAL, LootboxItemRareza.RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.DAMAGE_ALL, 4)),
                item(NORMAL, LootboxItemRareza.RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.SILK_TOUCH, 1)),
                item(NORMAL, LootboxItemRareza.RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.FROST_WALKER, 1)),
                item(NORMAL, LootboxItemRareza.RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.ARROW_DAMAGE, 3)),
                item(NORMAL, LootboxItemRareza.RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.LOOT_BONUS_MOBS, 2)),
                item(NORMAL, LootboxItemRareza.RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.LOOT_BONUS_BLOCKS, 2)),

                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.DIAMOND_ORE, 16, 32),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.TURTLE_EGG, 4, 8),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.WITHER_ROSE, 8, 16),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.BEDROCK, 1, 2),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.PLAYER_HEAD),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.CREEPER_HEAD),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.ZOMBIE_HEAD),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.SKELETON_SKULL),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.WITHER_SKELETON_SKULL, 1, 5),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.SLIME_BLOCK, 16, 32),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.BEACON, 2, 4),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.ENDER_CHEST, 4, 6),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 4)),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.MENDING, 1)),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.IRON_PICKAXE, Map.of(
                        Enchantment.SILK_TOUCH, 1,
                        Enchantment.DURABILITY, 3,
                        Enchantment.DIG_SPEED, 3
                )),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.BOW, Map.of(
                        Enchantment.DURABILITY, 2,
                        Enchantment.ARROW_FIRE, 1
                )),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.DIAMOND_PICKAXE, Map.of(
                        Enchantment.DURABILITY, 3,
                        Enchantment.DIG_SPEED, 4,
                        Enchantment.LOOT_BONUS_BLOCKS, 3
                )),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.DIAMOND_PICKAXE, Map.of(
                        Enchantment.DURABILITY, 1,
                        Enchantment.DIG_SPEED, 2,
                        Enchantment.LOOT_BONUS_BLOCKS, 1
                )),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.DIAMOND_SWORD, Map.of(
                        Enchantment.DURABILITY, 2,
                        Enchantment.FIRE_ASPECT, 1,
                        Enchantment.LOOT_BONUS_MOBS, 1,
                        Enchantment.DAMAGE_ALL, 3
                )),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.DIAMOND_SWORD, Map.of(
                        Enchantment.LOOT_BONUS_MOBS, 2,
                        Enchantment.DAMAGE_ALL, 1
                )),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.DIAMOND_SWORD, Map.of(
                        Enchantment.DAMAGE_ALL, 3,
                        Enchantment.DURABILITY, 2
                )),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.DIAMOND_CHESTPLATE, Map.of(
                        Enchantment.PROTECTION_ENVIRONMENTAL, 1
                )),
                item(NORMAL, LootboxItemRareza.MUY_RARO, Material.DIAMOND_HELMET, Map.of(
                        Enchantment.PROTECTION_ENVIRONMENTAL, 4
                ))
        );

        List<LootboxItem> lootboxRaro = List.of(
                item(RARO, LootboxItemRareza.MUY_COMUN, Material.EMERALD, 32, 48),
                item(RARO, LootboxItemRareza.MUY_COMUN, Material.EMERALD_BLOCK, 16, 32),
                item(RARO, LootboxItemRareza.MUY_COMUN, Material.LANTERN, 64, 64),
                item(RARO, LootboxItemRareza.MUY_COMUN, Material.IRON_BLOCK, 64, 64),
                item(RARO, LootboxItemRareza.MUY_COMUN, Material.ENCHANTING_TABLE, 8, 16),
                item(RARO, LootboxItemRareza.MUY_COMUN, Material.ITEM_FRAME, 32, 48),
                item(RARO, LootboxItemRareza.MUY_COMUN, Material.CHEST, 64, 64),
                item(RARO, LootboxItemRareza.MUY_COMUN, Material.NAME_TAG, 8, 16),
                item(RARO, LootboxItemRareza.MUY_COMUN, Material.BOOK, 16, 32),
                item(RARO, LootboxItemRareza.MUY_COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.FIRE_ASPECT, 2)),
                item(RARO, LootboxItemRareza.MUY_COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 3)),
                item(RARO, LootboxItemRareza.MUY_COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.DURABILITY, 2)),
                item(RARO, LootboxItemRareza.MUY_COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.DAMAGE_ALL, 3)),
                item(RARO, LootboxItemRareza.MUY_COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.ARROW_DAMAGE, 3)),
                item(RARO, LootboxItemRareza.MUY_COMUN, Material.SHULKER_BOX),

                item(RARO, LootboxItemRareza.COMUN, Material.EMERALD, 64, 64),
                item(RARO, LootboxItemRareza.COMUN, Material.EMERALD_BLOCK, 32, 48),
                item(RARO, LootboxItemRareza.COMUN, Material.DIAMOND, 32, 48),
                item(RARO, LootboxItemRareza.COMUN, Material.LAPIS_BLOCK, 16, 32),
                item(RARO, LootboxItemRareza.COMUN, Material.NETHERITE_SCRAP, 6, 12),
                item(RARO, LootboxItemRareza.COMUN, Material.NETHERITE_INGOT, 3, 6),
                item(RARO, LootboxItemRareza.COMUN, Material.OBSIDIAN, 32, 48),
                item(RARO, LootboxItemRareza.COMUN, Material.GLOWSTONE, 64, 64),
                item(RARO, LootboxItemRareza.COMUN, Material.BOOKSHELF, 32, 48),
                item(RARO, LootboxItemRareza.COMUN, Material.ANVIL, 48, 64),
                item(RARO, LootboxItemRareza.COMUN, Material.IRON_BLOCK, 48, 64),
                item(RARO, LootboxItemRareza.COMUN, Material.ENCHANTING_TABLE, 16, 32),
                item(RARO, LootboxItemRareza.COMUN, Material.ITEM_FRAME, 48, 64),
                item(RARO, LootboxItemRareza.COMUN, Material.HOPPER, 32, 48),
                item(RARO, LootboxItemRareza.COMUN, Material.OBSERVER, 32, 48),
                item(RARO, LootboxItemRareza.COMUN, Material.STICKY_PISTON, 16, 32),
                item(RARO, LootboxItemRareza.COMUN, Material.REPEATER, 64, 64),
                item(RARO, LootboxItemRareza.COMUN, Material.ARMOR_STAND, 16, 32),
                item(RARO, LootboxItemRareza.COMUN, Material.POWERED_RAIL, 32, 48),
                item(RARO, LootboxItemRareza.COMUN, Material.NETHERITE_PICKAXE),
                item(RARO, LootboxItemRareza.COMUN, Material.NETHERITE_SWORD),
                item(RARO, LootboxItemRareza.COMUN, Material.NETHERITE_AXE),
                item(RARO, LootboxItemRareza.COMUN, Material.NETHERITE_SHOVEL),
                item(RARO, LootboxItemRareza.COMUN, Material.NAME_TAG, 16, 32),
                item(RARO, LootboxItemRareza.COMUN, Material.FIREWORK_ROCKET, 64, 64),
                item(RARO, LootboxItemRareza.COMUN, Material.ENDER_CHEST, 2, 4),
                item(RARO, LootboxItemRareza.COMUN, Material.DIAMOND_HORSE_ARMOR),
                item(RARO, LootboxItemRareza.COMUN, Material.ENCHANTED_GOLDEN_APPLE, 2, 4),
                item(RARO, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.ARROW_KNOCKBACK, 3)),
                item(RARO, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 3)),
                item(RARO, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.FIRE_ASPECT, 2)),
                item(RARO, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.DURABILITY, 2)),
                item(RARO, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.DAMAGE_ALL, 3)),
                item(RARO, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.ARROW_DAMAGE, 3)),
                item(RARO, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.LOOT_BONUS_MOBS, 1)),
                item(RARO, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.LOOT_BONUS_BLOCKS, 1)),
                item(RARO, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.ARROW_FIRE, 1)),
                item(RARO, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.ARROW_INFINITE, 1)),
                item(RARO, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.SILK_TOUCH, 1)),

                item(RARO, LootboxItemRareza.COMUN, Material.ENCHANTED_BOOK, Map.of(Enchantment.FROST_WALKER, 1)),
                item(RARO, LootboxItemRareza.RARO, Material.LAPIS_BLOCK, 32, 48),
                item(RARO, LootboxItemRareza.RARO, Material.QUARTZ_BLOCK, 64, 64),
                item(RARO, LootboxItemRareza.RARO, Material.GOLD_BLOCK, 32, 48),
                item(RARO, LootboxItemRareza.RARO, Material.DIAMOND, 32, 48),
                item(RARO, LootboxItemRareza.RARO, Material.DIAMOND, 4, 8),
                item(RARO, LootboxItemRareza.RARO, Material.NETHERITE_SCRAP, 12, 21),
                item(RARO, LootboxItemRareza.RARO, Material.NETHERITE_INGOT, 6, 9),
                item(RARO, LootboxItemRareza.RARO, Material.OBSIDIAN, 64, 64),
                item(RARO, LootboxItemRareza.RARO, Material.DIAMOND_ORE, 16, 32),
                item(RARO, LootboxItemRareza.RARO, Material.ANCIENT_DEBRIS, 16, 32),
                item(RARO, LootboxItemRareza.RARO, Material.WITHER_ROSE, 8, 16),
                item(RARO, LootboxItemRareza.RARO, Material.BOOKSHELF, 64, 64),
                item(RARO, LootboxItemRareza.RARO, Material.ENDER_EYE, 12, 16),
                item(RARO, LootboxItemRareza.RARO, Material.WITHER_SKELETON_SKULL, 2, 6),
                item(RARO, LootboxItemRareza.RARO, Material.SLIME_BLOCK, 16, 32),
                item(RARO, LootboxItemRareza.RARO, Material.HOPPER, 48, 64),
                item(RARO, LootboxItemRareza.RARO, Material.OBSERVER, 48, 64),
                item(RARO, LootboxItemRareza.RARO, Material.STICKY_PISTON, 32, 48),
                item(RARO, LootboxItemRareza.RARO, Material.BEACON, 4, 6),
                item(RARO, LootboxItemRareza.RARO, Material.ENDER_CHEST, 4, 8),
                item(RARO, LootboxItemRareza.RARO, Material.NETHERITE_HELMET),
                item(RARO, LootboxItemRareza.RARO, Material.NETHERITE_CHESTPLATE),
                item(RARO, LootboxItemRareza.RARO, Material.NETHERITE_LEGGINGS),
                item(RARO, LootboxItemRareza.RARO, Material.NETHERITE_BOOTS),
                item(RARO, LootboxItemRareza.RARO, Material.ENCHANTED_GOLDEN_APPLE, 4, 8),
                item(RARO, LootboxItemRareza.RARO, Material.HEART_OF_THE_SEA, 4, 8),
                item(RARO, LootboxItemRareza.RARO, Material.NAUTILUS_SHELL, 4, 8),
                item(RARO, LootboxItemRareza.RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.DURABILITY, 3)),
                item(RARO, LootboxItemRareza.RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.DAMAGE_ALL, 4)),
                item(RARO, LootboxItemRareza.RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.FROST_WALKER, 2)),
                item(RARO, LootboxItemRareza.RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.ARROW_DAMAGE, 4)),
                item(RARO, LootboxItemRareza.RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.LOOT_BONUS_MOBS, 3)),
                item(RARO, LootboxItemRareza.RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.LOOT_BONUS_BLOCKS, 3)),
                item(RARO, LootboxItemRareza.RARO, Material.PLAYER_HEAD),
                item(RARO, LootboxItemRareza.RARO, Material.CREEPER_HEAD),
                item(RARO, LootboxItemRareza.RARO, Material.ZOMBIE_HEAD),
                item(RARO, LootboxItemRareza.RARO, Material.SKELETON_SKULL),

                item(RARO, LootboxItemRareza.MUY_RARO, Material.DIAMOND_ORE, 32, 48),
                item(RARO, LootboxItemRareza.MUY_RARO, Material.TURTLE_EGG, 8, 16),
                item(RARO, LootboxItemRareza.MUY_RARO, Material.WITHER_ROSE, 16, 32),
                item(RARO, LootboxItemRareza.MUY_RARO, Material.BEDROCK, 2, 8),
                item(RARO, LootboxItemRareza.MUY_RARO, Material.WITHER_SKELETON_SKULL, 5, 8),
                item(RARO, LootboxItemRareza.MUY_RARO, Material.TRIDENT),
                item(RARO, LootboxItemRareza.MUY_RARO, Material.ELYTRA),
                item(RARO, LootboxItemRareza.MUY_RARO, Material.SLIME_BLOCK, 32, 48),
                item(RARO, LootboxItemRareza.MUY_RARO, Material.BEACON, 6, 12),
                item(RARO, LootboxItemRareza.MUY_RARO, Material.ENDER_CHEST, 8, 16),
                item(RARO, LootboxItemRareza.MUY_RARO, Material.ENCHANTED_BOOK, Map.of(Enchantment.MENDING, 1)),
                item(RARO, LootboxItemRareza.MUY_RARO, Material.DIAMOND_PICKAXE, Map.of(
                        Enchantment.SILK_TOUCH, 1,
                        Enchantment.DURABILITY, 3,
                        Enchantment.DIG_SPEED, 5
                )),
                item(RARO, LootboxItemRareza.MUY_RARO, Material.BOW, Map.of(
                        Enchantment.DURABILITY, 3,
                        Enchantment.ARROW_FIRE, 1,
                        Enchantment.MENDING, 1
                )),
                item(RARO, LootboxItemRareza.MUY_RARO, Material.DIAMOND_PICKAXE, Map.of(
                        Enchantment.DURABILITY, 3,
                        Enchantment.DIG_SPEED, 4,
                        Enchantment.LOOT_BONUS_BLOCKS, 3
                )),
                item(RARO, LootboxItemRareza.MUY_RARO, Material.NETHERITE_PICKAXE, Map.of(
                        Enchantment.DURABILITY, 2,
                        Enchantment.DIG_SPEED, 4,
                        Enchantment.SILK_TOUCH, 1
                )),
                item(RARO, LootboxItemRareza.MUY_RARO, Material.NETHERITE_SWORD, Map.of(
                        Enchantment.DURABILITY, 2,
                        Enchantment.FIRE_ASPECT, 1,
                        Enchantment.LOOT_BONUS_MOBS, 2,
                        Enchantment.DAMAGE_ALL, 3
                )),
                item(RARO, LootboxItemRareza.MUY_RARO, Material.DIAMOND_CHESTPLATE, Map.of(
                        Enchantment.PROTECTION_ENVIRONMENTAL, 4,
                        Enchantment.MENDING, 1,
                        Enchantment.DURABILITY, 2
                )),
                item(RARO, LootboxItemRareza.MUY_RARO, Material.DIAMOND_HELMET, Map.of(
                        Enchantment.PROTECTION_ENVIRONMENTAL, 4,
                        Enchantment.DURABILITY, 2,
                        Enchantment.WATER_WORKER, 1
                ))
        );

        lootboxComun.forEach(lootboxItemsService::save);
        lootboxNormal.forEach(lootboxItemsService::save);
        lootboxRaro.forEach(lootboxItemsService::save);
    }

    private LootboxItem item(LootboxTier tier, LootboxItemRareza rareza, Material material) {
        return new LootboxItem(UUID.randomUUID(), tier, rareza, material.name(), 1, 1, new ItemMinecraftEncantamientos(new LinkedList<>()));
    }

    private LootboxItem item(LootboxTier tier, LootboxItemRareza rareza, Material material, int cantidadMinima, int cantidadMaxima) {
        return new LootboxItem(UUID.randomUUID(), tier, rareza, material.name(), cantidadMinima, cantidadMaxima, new ItemMinecraftEncantamientos(new LinkedList<>()));
    }

    private LootboxItem item(LootboxTier tier, LootboxItemRareza rareza, Material material, Map<Enchantment, Integer> en) {
        return new LootboxItem(UUID.randomUUID(), tier, rareza, material.name(), 1, 1, ItemMinecraftEncantamientos.fromMap(en));
    }
}
