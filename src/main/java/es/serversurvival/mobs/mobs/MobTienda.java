package es.serversurvival.mobs.mobs;

import es.serversurvival.menus.menus.OfertasMenu;
import es.serversurvival.mobs.InteractuableMob;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class MobTienda extends InteractuableMob {

    @Override
    public void onClick(PlayerInteractEntityEvent event) {
        OfertasMenu menu = new OfertasMenu(event.getPlayer());
        menu.openMenu();
    }

    @Override
    public Location getLocation() {
        return new Location(null, 243, 65, -203);
    }
}