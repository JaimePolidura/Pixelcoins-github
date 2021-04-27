package es.serversurvival.nfs.utils;

import es.jaimetruman.mobs.Mob;
import es.jaimetruman.mobs.OnPlayerInteractMob;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 245, y = 65, z = -193)
public class MobHome implements OnPlayerInteractMob {
    @Override
    public void execute(PlayerInteractEntityEvent event) {
        event.getPlayer().performCommand("home");
    }
}
