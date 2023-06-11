package es.serversurvival.minecraftserver.deudas.prestar;

import es.bukkitbettermenus.MenuService;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.minecraftserver.webaction.WebActionException;
import es.serversurvival.minecraftserver.webaction.WebActionHandler;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class PrestarWebActionHandler implements WebActionHandler<PrestarWebActionRequestBody> {
    private final MenuService menuService;

    @Override
    public void handle(UUID jugadorId, PrestarWebActionRequestBody body) throws WebActionException {
        Player deudorJugador = Bukkit.getPlayer(body.getNombreDelJugadorAPrestar());
        if(deudorJugador == null)
            throw new WebActionException(deudorJugador.getName() + " no esta online");

        menuService.open(deudorJugador, PrestarConfirmacionMenu.class, body.toPrestarConfirmacionMenuState(jugadorId));
    }
}
