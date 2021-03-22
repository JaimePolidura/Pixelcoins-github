package es.serversurvival.mobs.mobs;

import es.serversurvival.mobs.InteractuableMob;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class MobHome extends InteractuableMob {
    @Override
    public void onClick(PlayerInteractEntityEvent event) {
        event.getPlayer().performCommand("home");
    }

    @Override
    public Location getLocation() {
        return new Location(null, 245, 65, -193);
    }
}
