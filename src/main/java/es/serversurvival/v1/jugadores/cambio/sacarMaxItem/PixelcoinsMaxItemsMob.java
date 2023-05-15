package es.serversurvival.v1.jugadores.cambio.sacarMaxItem;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import lombok.AllArgsConstructor;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 261,y = 64, z = -211)
@AllArgsConstructor
public class PixelcoinsMaxItemsMob implements OnPlayerInteractMob {
    private final MenuService menuService;

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.menuService.open(event.getPlayer(), SacarMaxItemMenu.class);
    }
}
