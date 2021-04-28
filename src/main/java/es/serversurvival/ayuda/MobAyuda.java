package es.serversurvival.ayuda;

import es.jaimetruman.mobs.Mob;
import es.jaimetruman.mobs.OnPlayerInteractMob;
import es.serversurvival.ayuda.verayudas.AyudaMenu;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 259, y = 65, z = -193)
public class MobAyuda implements OnPlayerInteractMob {
    @Override
    public void execute(PlayerInteractEntityEvent event) {
        AyudaMenu menu = new AyudaMenu(event.getPlayer());
    }
}
