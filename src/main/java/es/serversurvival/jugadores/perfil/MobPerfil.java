package es.serversurvival.jugadores.perfil;

import es.jaimetruman.menus.MenuService;
import es.jaimetruman.mobs.Mob;
import es.jaimetruman.mobs.OnPlayerInteractMob;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 252, y = 65, z = -207)
public class MobPerfil implements OnPlayerInteractMob {
    private final MenuService menuService;

    public MobPerfil() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.menuService.open(event.getPlayer(), new PerfilMenu(event.getPlayer().getName()));
    }
}
