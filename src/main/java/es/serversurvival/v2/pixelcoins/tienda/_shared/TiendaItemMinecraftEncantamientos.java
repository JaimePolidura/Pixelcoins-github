package es.serversurvival.v2.pixelcoins.tienda._shared;

import es.serversurvival.v1._shared.utils.Funciones;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public final class TiendaItemMinecraftEncantamientos {
    @Getter private final String nombre;
    @Getter private final int nivel;

    @Override
    @SneakyThrows
    public String toString() {
        return Funciones.MAPPER.writeValueAsString(this);
    }
}
