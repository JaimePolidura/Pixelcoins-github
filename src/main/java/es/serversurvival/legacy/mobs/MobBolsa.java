package es.serversurvival.legacy.mobs;

import es.jaimetruman.mobs.Mob;
import es.jaimetruman.mobs.OnPlayerInteractMob;
import es.serversurvival.legacy.menus.menus.ElegirInversionMenu;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 243, y = 65, z = -199)
public class MobBolsa implements OnPlayerInteractMob {
    @Override
    public void execute(PlayerInteractEntityEvent event) {
        ElegirInversionMenu menu = new ElegirInversionMenu(event.getPlayer());
    }
}
