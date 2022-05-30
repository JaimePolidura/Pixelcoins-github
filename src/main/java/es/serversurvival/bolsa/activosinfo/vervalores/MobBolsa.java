package es.serversurvival.bolsa.activosinfo.vervalores;

import es.jaimetruman.menus.MenuService;
import es.jaimetruman.mobs.Mob;
import es.jaimetruman.mobs.OnPlayerInteractMob;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 243, y = 65, z = -199)
public class MobBolsa implements OnPlayerInteractMob {
    private final MenuService menuService;

    public MobBolsa() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.menuService.open(event.getPlayer(), new ElegirInversionMenu());
    }
}
