package es.serversurvival.jugadores.cambio.sacarItem;

import es.bukkitclassmapper.menus.MenuService;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 243, y = 64, z = -211)
public class SacarItemMob implements OnPlayerInteractMob {
    private final MenuService menuService;

    public SacarItemMob() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.menuService.open(event.getPlayer(), new SacarItemMenu(event.getPlayer().getName()));
    }
}
