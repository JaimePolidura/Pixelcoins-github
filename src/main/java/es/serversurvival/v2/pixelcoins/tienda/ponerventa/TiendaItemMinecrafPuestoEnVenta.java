package es.serversurvival.v2.pixelcoins.tienda.ponerventa;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@AllArgsConstructor
public final class TiendaItemMinecrafPuestoEnVenta extends PixelcoinsEvento {
    @Getter private final ItemStack item;
    @Getter private final double precio;
    @Getter private final UUID jugadorId;
}
