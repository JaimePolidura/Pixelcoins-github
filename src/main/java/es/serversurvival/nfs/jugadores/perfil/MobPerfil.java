package es.serversurvival.nfs.jugadores.perfil;

import es.jaimetruman.mobs.Mob;
import es.jaimetruman.mobs.OnPlayerInteractMob;
import es.serversurvival.nfs.jugadores.perfil.PerfilMenu;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 252, y = 65, z = -207)
public class MobPerfil implements OnPlayerInteractMob {
    @Override
    public void execute(PlayerInteractEntityEvent event) {
        PerfilMenu perfilMenu = new PerfilMenu(event.getPlayer());
    }
}
