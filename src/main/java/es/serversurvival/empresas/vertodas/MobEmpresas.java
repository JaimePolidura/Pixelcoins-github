package es.serversurvival.empresas.vertodas;

import es.jaimetruman.mobs.Mob;
import es.jaimetruman.mobs.OnPlayerInteractMob;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 261, y = 65, z = -199)
public class MobEmpresas implements OnPlayerInteractMob {
    @Override
    public void execute(PlayerInteractEntityEvent event) {
        EmpresasVerTodasMenu empresasMenu = new EmpresasVerTodasMenu(event.getPlayer());
    }
}
