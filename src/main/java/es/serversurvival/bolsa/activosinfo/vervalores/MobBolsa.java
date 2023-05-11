package es.serversurvival.bolsa.activosinfo.vervalores;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.posicionesabiertas.comprarlargo.ComprarLargoUseCase;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import lombok.AllArgsConstructor;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.concurrent.ExecutorService;

@Mob(x = 243, y = 65, z = -199)
@AllArgsConstructor
public class MobBolsa implements OnPlayerInteractMob {
    private final ComprarLargoUseCase comprarLargoUseCase;
    private final ActivosInfoService activosInfoService;
    private final JugadoresService jugadoresService;
    private final ExecutorService executor;
    private final MenuService menuService;

    @Override
    public void execute(PlayerInteractEntityEvent event) {
        this.menuService.open(event.getPlayer(), new ElegirInversionMenu(
                comprarLargoUseCase, activosInfoService, jugadoresService, executor, menuService
        ));
    }
}
