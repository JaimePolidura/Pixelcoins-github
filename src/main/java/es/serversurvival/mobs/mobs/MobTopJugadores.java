package es.serversurvival.mobs.mobs;

import es.serversurvival.menus.menus.TopMenu;
import es.serversurvival.mobs.InteractuableMob;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class MobTopJugadores extends InteractuableMob {
    @Override
    public void onClick(PlayerInteractEntityEvent event) {
        TopMenu topMenu = new TopMenu(event.getPlayer());
        topMenu.openMenu();
    }

    @Override
    public Location getLocation() {
        return new Location(null,261,65,-203);
    }
}
