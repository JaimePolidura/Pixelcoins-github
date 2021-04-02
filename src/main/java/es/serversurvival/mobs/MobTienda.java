package es.serversurvival.mobs;

import es.jaimetruman.mobs.Mob;
import es.jaimetruman.mobs.OnPlayerInteractMob;
import es.serversurvival.menus.menus.OfertasMenu;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 243, y = 65, z = -203)
public class MobTienda implements OnPlayerInteractMob {
    @Override
    public void execute(PlayerInteractEntityEvent event) {
        OfertasMenu menu = new OfertasMenu(event.getPlayer());
    }
}
