package es.serversurvival.mobs.mobs.withers;

import es.serversurvival.menus.menus.SacarItemMenu;
import es.serversurvival.mobs.InteractuableMob;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PixelcoinsItemMob extends InteractuableMob {
    @Override
    public void onClick(PlayerInteractEntityEvent event) {
        SacarItemMenu menu = new SacarItemMenu(event.getPlayer());
    }

    @Override
    public Location getLocation() {
        return new Location(null, 243, 64, -211);
    }
}
