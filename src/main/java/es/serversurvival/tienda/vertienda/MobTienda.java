package es.serversurvival.tienda.vertienda;

import es.bukkitclassmapper.menus.MenuService;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.tienda.vertienda.menu.TiendaMenu;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 243, y = 65, z = -203)
public class MobTienda implements OnPlayerInteractMob {
    private final MenuService menuService;

    public MobTienda() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.menuService.open(event.getPlayer(), new TiendaMenu(event.getPlayer().getName()));
    }
}
