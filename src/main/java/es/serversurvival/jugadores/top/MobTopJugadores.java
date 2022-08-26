package es.serversurvival.jugadores.top;

import es.bukkitclassmapper.menus.MenuService;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 261, y = 65, z = -203)
public class MobTopJugadores implements OnPlayerInteractMob {
    private final MenuService menuService;

    public MobTopJugadores() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.menuService.open(event.getPlayer(), new TopMenu());
    }
}
