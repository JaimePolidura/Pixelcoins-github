package es.serversurvival.minecraftserver.jugadores.mobs;

import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 55, y = 64, z = 117)
public final class MobAyuda implements OnPlayerInteractMob {
    @Override
    public void execute(PlayerInteractEntityEvent event) {
        event.getPlayer().performCommand("ayuda");
    }
}
