package es.serversurvival.pixelcoins.lootbox._shared.items.domain;

import es.bukkitbettermenus.utils.ItemBuilder;
import es.serversurvival._shared.items.ItemMinecraftEncantamientos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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

    public ItemStack toItemStack() {
        return ItemBuilder.of(Material.valueOf(nombre))
                .addEnchanments(encantamientos.toEnchantments())
                .amount(cantidadMinima)
                .build();
    }

    public int getCantidadAleatoria() {
        return (int) ((Math.random() * cantidadMaxima - cantidadMinima + 1) + cantidadMinima);
    }

    public boolean puedeTenerVariasCantidades() {
        return cantidadMinima != cantidadMaxima;
    }
}
