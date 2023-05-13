package es.serversurvival.jugadores.cambio.ingresarItem.lapislazuli;

import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import es.serversurvival.jugadores.cambio.TipoCambioPixelcoins;
import es.serversurvival.jugadores.cambio.ingresarItem.IngresadorItem;
import lombok.AllArgsConstructor;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 259, y = 64, z = -216)
@AllArgsConstructor
public class IngresarLapisPixelcoinsMob implements OnPlayerInteractMob {
    private final IngresadorItem ingresadorItem;

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.ingresadorItem.ingresarItemInMano(event.getPlayer(), TipoCambioPixelcoins.LAPIS_LAZULI, TipoCambioPixelcoins.LAPIS_BLOCK);
    }
}
