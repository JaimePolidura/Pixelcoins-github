package es.serversurvival.minecraftserver.empresas.contratar;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.minecraftserver.webaction.WebActionException;
import es.serversurvival.minecraftserver.webaction.WebActionHandler;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas.contratar.ContratarEmpleadoParametros;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.AllArgsConstructor;

import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival.minecraftserver._shared.MinecraftUtils.enviarMensajeYSonido;
import static org.bukkit.ChatColor.*;
import static org.bukkit.Sound.*;

@Service
@AllArgsConstructor
public final class ContratarEmpleadoWebActionHandler implements WebActionHandler<ContratarEmpleadoWebActionRequestBody> {
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;
    private final UseCaseBus useCaseBus;

    @Override
    public void handle(UUID jugadorId, ContratarEmpleadoWebActionRequestBody body) throws WebActionException {
        Empresa empresa = empresasService.getByNombre(body.getNombreDeLaEmpresa());
        UUID jugadorIdAContratar = jugadoresService.getByNombre(body.getNombreJugadorAContratar())
                .getJugadorId();

        useCaseBus.handle(ContratarEmpleadoParametros.builder()
                .jugadorIdAContratar(jugadorIdAContratar)
                .descripccion(body.getDescripccion())
                .empresaId(empresa.getEmpresaId())
                .jugadorIdContrador(jugadorId)
                .periodoPagoMs(body.getPeriodoPagoEnSegundos())
                .sueldo(body.getSueldo())
                .build());

        enviarMensajeYSonido(jugadorId, GOLD + "Has contratado a " + body.getNombreJugadorAContratar() +
                " en la empresa " + body.getNombreDeLaEmpresa(), ENTITY_PLAYER_LEVELUP) ;
        enviarMensajeYSonido(jugadorIdAContratar, GOLD + "Has sido contratado en " + body.getNombreDeLaEmpresa() + "con el cargo " +
                body.getDescripccion() + " con un sueldo de " + GREEN + FORMATEA.format(body.getSueldo()) + " PC / " +
                millisToDias(body.getPeriodoPagoEnSegundos()) + " dias", ENTITY_PLAYER_LEVELUP);
    }
}
