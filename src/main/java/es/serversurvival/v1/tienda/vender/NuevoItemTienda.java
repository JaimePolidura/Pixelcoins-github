package es.serversurvival.v1.tienda.vender;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@AllArgsConstructor
public final class NuevoItemTienda extends PixelcoinsEvento {
    @Getter private final UUID tiendaObjetoId;
    @Getter private final String jugador;
    @Getter private final ItemStack item;
}
