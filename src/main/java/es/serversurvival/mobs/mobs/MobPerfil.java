package es.serversurvival.mobs.mobs;

import es.serversurvival.menus.menus.PerfilMenu;
import es.serversurvival.mobs.InteractuableMob;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class MobPerfil extends InteractuableMob {
    @Override
    public void onClick(PlayerInteractEntityEvent event) {
        PerfilMenu perfilMenu = new PerfilMenu(event.getPlayer());
        perfilMenu.openMenu();
    }

    @Override
    public Location getLocation() {
        return new Location(null, 252, 65, -207);
    }
}