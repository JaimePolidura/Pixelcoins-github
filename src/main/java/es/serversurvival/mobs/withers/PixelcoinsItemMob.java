package es.serversurvival.mobs.withers;

import es.jaimetruman.mobs.Mob;
import es.jaimetruman.mobs.OnPlayerInteractMob;
import es.serversurvival.menus.menus.SacarItemMenu;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 243, y = 64, z = -211)
public class PixelcoinsItemMob implements OnPlayerInteractMob {
    @Override
    public void execute(PlayerInteractEntityEvent event) {
        SacarItemMenu menu = new SacarItemMenu(event.getPlayer());
    }
}
