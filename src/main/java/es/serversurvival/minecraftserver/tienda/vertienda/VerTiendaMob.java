package es.serversurvival.minecraftserver.tienda.vertienda;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import lombok.AllArgsConstructor;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 243, y = 65, z = -203)
@AllArgsConstructor
public final class VerTiendaMob implements OnPlayerInteractMob {
    private final MenuService menuService;

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.menuService.open(event.getPlayer(), TiendaMenu.class);
    }
}
