package es.serversurvival.mobs.mobs;

import es.serversurvival.menus.menus.EmpresasVerTodasMenu;
import es.serversurvival.mobs.InteractuableMob;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class MobEmpresas extends InteractuableMob {
    @Override
    public void onClick(PlayerInteractEntityEvent event) {
        EmpresasVerTodasMenu empresasMenu = new EmpresasVerTodasMenu(event.getPlayer());
    }

    @Override
    public Location getLocation() {
        return new Location(null,261,65,-199);
    }
}
