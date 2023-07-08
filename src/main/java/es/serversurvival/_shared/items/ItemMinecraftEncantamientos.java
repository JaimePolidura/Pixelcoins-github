package es.serversurvival._shared.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public final class ItemMinecraftEncantamientos {
    @Getter private final List<ItemMinecraftEncantamiento> encantamientos;

    public Map<Enchantment, Integer> toEnchantments() {
        return encantamientos.stream()
                .collect(Collectors.toMap(
                        a -> Enchantment.getByName(a.getNombre()),
                        ItemMinecraftEncantamiento::getNivel)
                );
    }

    public static ItemMinecraftEncantamientos fromItem(ItemStack item) {
        Map<Enchantment, Integer> enchantmentsBukkitTypes = item.getType() == Material.ENCHANTED_BOOK ?
                ((EnchantmentStorageMeta) item.getItemMeta()).getStoredEnchants() :
                item.getEnchantments();

        return new ItemMinecraftEncantamientos(enchantmentsBukkitTypes.keySet().stream()
                .map(e -> new ItemMinecraftEncantamiento(e.getName(), enchantmentsBukkitTypes.get(e)))
                .toList());
    }

    public static ItemMinecraftEncantamientos fromMap(Map<Enchantment, Integer> enchants) {
        return new ItemMinecraftEncantamientos(enchants.keySet().stream()
                .map(e -> new ItemMinecraftEncantamiento(e.getName(), enchants.get(e)))
                .toList());
    }
}
