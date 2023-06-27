package es.serversurvival.minecraftserver.deudas.prestar;

import es.bukkitbettermenus.MenuService;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver.webaction.WebActionException;
import es.serversurvival.minecraftserver.webaction.WebActionHandler;
import es.serversurvival.pixelcoins.deudas.prestar.PrestarDeudaParametros;
import es.serversurvival.pixelcoins.deudas.prestar.PrestarDeudaUseCase;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class PrestarWebActionHandler implements WebActionHandler<PrestarWebActionRequestBody> {
    private final PrestarDeudaUseCase prestarDeudaUseCase;
    private final MenuService menuService;

    @Override
    public void handle(UUID jugadorId, PrestarWebActionRequestBody body) throws WebActionException {
        Player deudorJugador = Bukkit.getPlayer(body.getNombreDelJugadorPrestar());
        validar(jugadorId, body, deudorJugador);

        Bukkit.getScheduler().runTask(Pixelcoin.INSTANCE, () -> {
            menuService.open(deudorJugador, PrestarConfirmacionMenu.class, body.toPrestarConfirmacionMenuState(jugadorId));
        });
    }

    private void validar(UUID jugadorId, PrestarWebActionRequestBody body, Player deudorJugador) throws WebActionException {
        if(deudorJugador == null)
            throw new WebActionException(body.getNombreDelJugadorPrestar() + " no esta online");

        prestarDeudaUseCase.validar(PrestarDeudaParametros.builder()
                        .acredorJugadorId(jugadorId)
                        .deudorJugadorId(deudorJugador.getUniqueId())
                        .interes(body.getInteres() * 100)
                        .nominal(body.getPixelcoins())
                        .numeroCuotasTotales(body.getNumeroCuotasTotales())
                        .periodoPagoCuota(Funciones.diasToMillis(body.getPeriodoPagoCuotaEnDias()))
                .build());
    }
}
