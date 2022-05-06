package es.serversurvival.tienda.vender;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@AllArgsConstructor
public final class NuevoTiendaObjetoAVender extends PixelcoinsEvento {
    @Getter private final UUID tiendaObjetoId;
    @Getter private final String jugador;
    @Getter private final ItemStack item;
}
