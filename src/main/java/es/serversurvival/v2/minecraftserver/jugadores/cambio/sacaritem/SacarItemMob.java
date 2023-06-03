package es.serversurvival.v2.minecraftserver.jugadores.cambio.sacaritem;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import lombok.AllArgsConstructor;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 243, y = 64, z = -211)
@AllArgsConstructor
public class SacarItemMob implements OnPlayerInteractMob {
    private final MenuService menuService;

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.menuService.open(event.getPlayer(), SacarItemMenu.class);
    }
}
