package es.serversurvival.minecraftserver.jugadores.mobs;

import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 57, y = 64, z = 100)
public final class MobPerfil implements OnPlayerInteractMob {
    @Override
    public void execute(PlayerInteractEntityEvent event) {
        event.getPlayer().performCommand("perfil");
    }
}
