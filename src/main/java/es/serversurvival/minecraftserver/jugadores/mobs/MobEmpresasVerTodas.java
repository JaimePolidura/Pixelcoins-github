package es.serversurvival.minecraftserver.jugadores.mobs;

import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 51, y = 64, z = 114)
public final class MobEmpresasVerTodas implements OnPlayerInteractMob {
    @Override
    public void execute(PlayerInteractEntityEvent entityEvent) {
        entityEvent.getPlayer().performCommand("empresas vertodas");
    }
}
