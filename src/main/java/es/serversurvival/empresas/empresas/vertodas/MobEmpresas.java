package es.serversurvival.empresas.empresas.vertodas;

import es.jaimetruman.menus.MenuService;
import es.jaimetruman.mobs.Mob;
import es.jaimetruman.mobs.OnPlayerInteractMob;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empleados.misempleos.VerEmpleosMenu;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 261, y = 65, z = -199)
public class MobEmpresas implements OnPlayerInteractMob {
    private final MenuService menuService;

    public MobEmpresas() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.menuService.open(event.getPlayer(), new VerTodasEmpresasMenu(event.getPlayer()));
    }
}
