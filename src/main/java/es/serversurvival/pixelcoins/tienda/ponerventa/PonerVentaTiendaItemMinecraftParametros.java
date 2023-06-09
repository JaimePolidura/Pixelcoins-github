package es.serversurvival.pixelcoins.tienda.ponerventa;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class PonerVentaTiendaItemMinecraftParametros implements ParametrosUseCase {
    @Getter private final ItemStack item;
    @Getter private final UUID jugadorId;
    @Getter private final double precio;
}
