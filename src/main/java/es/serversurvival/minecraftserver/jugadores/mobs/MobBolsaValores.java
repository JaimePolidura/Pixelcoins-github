package es.serversurvival.minecraftserver.jugadores.mobs;

import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 63, y = 64, z = 106)
public final class MobBolsaValores implements OnPlayerInteractMob {
    @Override
    public void execute(PlayerInteractEntityEvent event) {
        event.getPlayer().performCommand("bolsa valores");
    }
}
