package es.serversurvival.v2.minecraftserver.empresas.contratar;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v2.minecraftserver.webaction.WebActionException;
import es.serversurvival.v2.minecraftserver.webaction.WebActionHandler;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.v2.pixelcoins.empresas.contratar.ContratarEmpleadoParametros;
import es.serversurvival.v2.pixelcoins.empresas.contratar.ContratarEmpleadoUseCase;
import es.serversurvival.v2.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.AllArgsConstructor;

import java.util.UUID;

import static es.serversurvival.v1._shared.utils.Funciones.*;
import static es.serversurvival.v2.minecraftserver._shared.MinecraftUtils.enviarMensajeYSonido;
import static org.bukkit.ChatColor.*;
import static org.bukkit.Sound.*;

@Service
@AllArgsConstructor
public final class ContratarEmpleadoWebActionHandler implements WebActionHandler<ContratarEmpleadoWebActionRequestBody> {
    private final ContratarEmpleadoUseCase contratarEmpleadoUseCase;
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;

    @Override
    public void handle(UUID jugadorId, ContratarEmpleadoWebActionRequestBody body) throws WebActionException {
        Empresa empresa = empresasService.getByNombre(body.getEmpresa());
        UUID jugadorIdAContratar = jugadoresService.getByNombre(body.getJugadorNombreAContratar())
                .getJugadorId();

        contratarEmpleadoUseCase.contratar(ContratarEmpleadoParametros.builder()
                .jugadorIdAContratar(jugadorIdAContratar)
                .descripccion(body.getDescripccion())
                .empresaId(empresa.getEmpresaId())
                .jugadorIdContrador(jugadorId)
                .periodoPagoMs(body.getPeriodoPagoMs())
                .sueldo(body.getSueldo())
                .build());

        enviarMensajeYSonido(jugadorId, GOLD + "Has contratado a " + body.getJugadorNombreAContratar() +
                " en la empresa " + body.getEmpresa(), ENTITY_PLAYER_LEVELUP) ;
        enviarMensajeYSonido(jugadorIdAContratar, GOLD + "Has sido contratado en " + body.getEmpresa() + "con el cargo " +
                body.getDescripccion() + " con un sueldo de " + GREEN + FORMATEA.format(body.getSueldo()) + " PC / " +
                millisToDias(body.getPeriodoPagoMs()) + " dias", ENTITY_PLAYER_LEVELUP);
    }
}
