package es.serversurvival.ayuda;

import es.bukkitclassmapper.menus.MenuService;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.ayuda.verayudas.VerAyudasMenu;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 259, y = 65, z = -193)
public class MobAyuda implements OnPlayerInteractMob {
    private final MenuService menuService;

    public MobAyuda() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }
    
    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.menuService.open(event.getPlayer(), new VerAyudasMenu());
    }
}
