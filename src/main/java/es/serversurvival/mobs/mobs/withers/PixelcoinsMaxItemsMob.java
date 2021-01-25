package es.serversurvival.mobs.mobs.withers;

import es.serversurvival.menus.menus.SacarMaxItemMenu;
import es.serversurvival.mobs.InteractuableMob;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PixelcoinsMaxItemsMob extends InteractuableMob {
    @Override
    public void onClick(PlayerInteractEntityEvent event) {
        SacarMaxItemMenu maxItemMenu = new SacarMaxItemMenu(event.getPlayer());
    }

    @Override
    public Location getLocation() {
        return new Location(null, 261, 64, -211);
    }
}
