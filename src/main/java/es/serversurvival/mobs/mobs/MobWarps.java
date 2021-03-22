package es.serversurvival.mobs.mobs;

import es.serversurvival.mobs.InteractuableMob;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class MobWarps extends InteractuableMob {
    @Override
    public void onClick(PlayerInteractEntityEvent event) {
        event.getPlayer().performCommand("warp");
    }

    @Override
    public Location getLocation() {
        return new Location(null, 252, 65, -193);
    }
}
