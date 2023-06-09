package es.serversurvival.minecraftserver.jugadores.perfil;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 252, y = 65, z = -207)
@RequiredArgsConstructor
public class MobPerfil implements OnPlayerInteractMob {
    private final MenuService menuService;

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.menuService.open(event.getPlayer(), PerfilMenu.class);
    }
}
