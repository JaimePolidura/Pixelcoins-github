package es.serversurvival.minecraftserver.jugadores.mobs;

import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 63, y = 64, z = 110)
public final class MobLootboxComprar implements OnPlayerInteractMob {
    @Override
    public void execute(PlayerInteractEntityEvent event) {
        event.getPlayer().performCommand("lootbox comprar");
    }
}
