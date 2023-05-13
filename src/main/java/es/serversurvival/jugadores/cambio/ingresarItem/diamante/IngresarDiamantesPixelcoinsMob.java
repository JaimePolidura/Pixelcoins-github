package es.serversurvival.jugadores.cambio.ingresarItem.diamante;

import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import es.serversurvival.jugadores.cambio.TipoCambioPixelcoins;
import es.serversurvival.jugadores.cambio.ingresarItem.IngresadorItem;
import lombok.AllArgsConstructor;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 245, y = 64, z = -216)
@AllArgsConstructor
public class IngresarDiamantesPixelcoinsMob implements OnPlayerInteractMob {
    private final IngresadorItem ingresadorItem;

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.ingresadorItem.ingresarItemInMano(event.getPlayer(), TipoCambioPixelcoins.DIAMOND, TipoCambioPixelcoins.DIAMOND_BLOCK);
    }
}
