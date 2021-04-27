package es.serversurvival.nfs.jugadores.top;

import es.jaimetruman.mobs.Mob;
import es.jaimetruman.mobs.OnPlayerInteractMob;
import es.serversurvival.nfs.jugadores.top.TopMenu;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 261, y = 65, z = -203)
public class MobTopJugadores implements OnPlayerInteractMob {
    @Override
    public void execute(PlayerInteractEntityEvent event) {
        TopMenu topMenu = new TopMenu(event.getPlayer());
    }
}
