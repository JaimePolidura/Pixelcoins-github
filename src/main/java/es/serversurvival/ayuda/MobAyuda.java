package es.serversurvival.ayuda;

import es.bukkitclassmapper.menus.MenuService;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.ayuda.verayudas.VerAyudasMenu;
import lombok.AllArgsConstructor;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.checkerframework.common.util.report.qual.ReportCall;

@Mob(x = 259, y = 65, z = -193)
@AllArgsConstructor
public class MobAyuda implements OnPlayerInteractMob {
    private final MenuService menuService;

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.menuService.open(event.getPlayer(), new VerAyudasMenu());
    }
}
