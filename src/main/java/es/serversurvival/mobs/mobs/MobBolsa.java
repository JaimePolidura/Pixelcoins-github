package es.serversurvival.mobs.mobs;

import es.serversurvival.menus.menus.ElegirInversionMenu;
import es.serversurvival.mobs.InteractuableMob;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class MobBolsa extends InteractuableMob {
    @Override
    public void onClick(PlayerInteractEntityEvent event) {
        ElegirInversionMenu menu = new ElegirInversionMenu(event.getPlayer());
        menu.openMenu();
    }

    @Override
    public Location getLocation() {
        return new Location(null, 243, 65, -199);
    }
}
