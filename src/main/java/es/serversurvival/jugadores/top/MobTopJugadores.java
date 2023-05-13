package es.serversurvival.jugadores.top;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 261, y = 65, z = -203)
@RequiredArgsConstructor
public class MobTopJugadores implements OnPlayerInteractMob {
    private final MenuService menuService;

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.menuService.open(event.getPlayer(), TopMenu.class);
    }
}
