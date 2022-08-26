package es.serversurvival.empresas.empresas.vertodas;

import es.bukkitclassmapper.menus.MenuService;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import es.serversurvival._shared.DependecyContainer;
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
