package es.serversurvival.mobs.mobs;

import es.serversurvival.menus.menus.AyudaMenu;
import es.serversurvival.mobs.InteractuableMob;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class MobAyuda extends InteractuableMob {
    @Override
    public void onClick(PlayerInteractEntityEvent event) {
        AyudaMenu menu = new AyudaMenu(event.getPlayer());
        menu.openMenu();
    }

    @Override
    public Location getLocation() {
        return new Location(null, 259, 65, -193);
    }
}