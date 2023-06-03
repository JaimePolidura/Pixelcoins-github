package es.serversurvival.v2.minecraftserver.jugadores.cambio.ingresaritem.lapislazuli;

import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import es.serversurvival.v2.minecraftserver.jugadores.cambio.ingresaritem.IngresadorItem;
import es.serversurvival.v2.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
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
