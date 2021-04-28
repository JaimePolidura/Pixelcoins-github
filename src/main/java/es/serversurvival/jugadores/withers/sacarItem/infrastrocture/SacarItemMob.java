package es.serversurvival.jugadores.withers.sacarItem.infrastrocture;

import es.jaimetruman.mobs.Mob;
import es.jaimetruman.mobs.OnPlayerInteractMob;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 243, y = 64, z = -211)
public class SacarItemMob implements OnPlayerInteractMob {
    @Override
    public void execute(PlayerInteractEntityEvent event) {
        SacarItemMenu menu = new SacarItemMenu(event.getPlayer());
    }
}
