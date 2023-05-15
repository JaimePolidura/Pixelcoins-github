package es.serversurvival.v1.jugadores.cambio.sacarItem;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import lombok.AllArgsConstructor;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 243, y = 64, z = -211)
@AllArgsConstructor
public class SacarItemMob implements OnPlayerInteractMob {
    private final JugadoresService jugadoresService;
    private final MenuService menuService;

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.menuService.open(event.getPlayer(), SacarItemMenu.class, this.jugadoresService.getByNombre(event.getPlayer().getName()));
    }
}
