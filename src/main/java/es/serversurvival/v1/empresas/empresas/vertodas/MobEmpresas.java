package es.serversurvival.v1.empresas.empresas.vertodas;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 261, y = 65, z = -199)
@RequiredArgsConstructor
public class MobEmpresas implements OnPlayerInteractMob {
    private final MenuService menuService;

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.menuService.open(event.getPlayer(), (Class<? extends Menu<?>>) VerTodasEmpresasMenu.class);
    }
}
