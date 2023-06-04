package es.serversurvival.v2.pixelcoins.tienda.ponerventa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class PonerVentaTiendaItemMinecraftParametros {
    @Getter private final ItemStack item;
    @Getter private final UUID jugadorId;
    @Getter private final double precio;
}
