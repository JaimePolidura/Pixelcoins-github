package es.serversurvival.mobs.withers;

import es.jaimetruman.mobs.Mob;
import es.jaimetruman.mobs.OnPlayerInteractMob;
import es.serversurvival.menus.menus.SacarMaxItemMenu;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 261,y = 64, z = -211)
public class PixelcoinsMaxItemsMob implements OnPlayerInteractMob {
    @Override
    public void execute(PlayerInteractEntityEvent event) {
        SacarMaxItemMenu maxItemMenu = new SacarMaxItemMenu(event.getPlayer());
    }
}
