package es.serversurvival.shared.utils;

import es.jaimetruman.mobs.Mob;
import es.jaimetruman.mobs.OnPlayerInteractMob;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 252, y = 65, z = -193)
public class MobWarps implements OnPlayerInteractMob {
    @Override
    public void execute(PlayerInteractEntityEvent event) {
        event.getPlayer().performCommand("warp");
    }
}
