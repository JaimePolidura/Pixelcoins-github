package es.serversurvival.v2.minecraftserver.jugadores.perfil;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@Mob(x = 252, y = 65, z = -207)
@RequiredArgsConstructor
public class MobPerfil implements OnPlayerInteractMob {
    private final JugadoresService jugadoresService;
    private final MenuService menuService;

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.menuService.open(event.getPlayer(), PerfilMenu.class, this.jugadoresService.getByNombre(event.getPlayer().getName()));
    }
}
