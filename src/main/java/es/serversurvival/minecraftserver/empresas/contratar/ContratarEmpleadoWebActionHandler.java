package es.serversurvival.minecraftserver.empresas.contratar;

import es.bukkitbettermenus.MenuService;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.Pixelcoin;
import es.serversurvival.minecraftserver.webaction.WebActionException;
import es.serversurvival.minecraftserver.webaction.WebActionHandler;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas.contratar.ContratarEmpleadoParametros;
import es.serversurvival.pixelcoins.empresas.contratar.ContratarEmpleadoUseCase;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.Jugador;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.*;

@Service
@AllArgsConstructor
public final class ContratarEmpleadoWebActionHandler implements WebActionHandler<ContratarEmpleadoWebActionRequestBody> {
    private final ContratarEmpleadoUseCase contratarEmpleadoUseCase;
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;
    private final MenuService menuService;

    @Override
    public void handle(UUID jugadorIdContratador, ContratarEmpleadoWebActionRequestBody body) throws WebActionException {
        Player playerAContratar = Bukkit.getPlayer(body.getNombreJugadorContratar());
        if(playerAContratar == null){
            throw new WebActionException(String.format("%s no esta en online", body.getNombreJugadorContratar()));
        }

        Empresa empresa = empresasService.getByNombre(body.getNombreDeLaEmpresa());
        Jugador jugadorAContratar = jugadoresService.getByNombre(body.getNombreJugadorContratar());

        contratarEmpleadoUseCase.validar(ContratarEmpleadoParametros.builder()
                .jugadorIdAContratar(jugadorAContratar.getJugadorId())
                .descripccion(body.getDescripccion())
                .empresaId(empresa.getEmpresaId())
                .jugadorId(jugadorIdContratador)
                .periodoPagoMs(diasToMillis(body.getPeriodoPagoEnDias()))
                .sueldo(body.getSueldo())
                .build());

        Bukkit.getScheduler().runTask(Pixelcoin.INSTANCE, () -> {
            menuService.open(playerAContratar, ConfirmarOfertaContratacionMenu.class, new ConfirmarOfertaContratacionMenu.State(
                    jugadorIdContratador, empresa, body.getSueldo(), diasToMillis(body.getPeriodoPagoEnDias()), body.getDescripccion()));
        });
    }
}
