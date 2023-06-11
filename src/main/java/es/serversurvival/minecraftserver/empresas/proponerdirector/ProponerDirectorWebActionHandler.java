package es.serversurvival.minecraftserver.empresas.proponerdirector;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.minecraftserver.webaction.WebActionException;
import es.serversurvival.minecraftserver.webaction.WebActionHandler;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas.cambiardirector.proponer.ProponerNuevoDirectorParametros;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.AllArgsConstructor;

import java.util.UUID;

import static es.serversurvival.minecraftserver._shared.MinecraftUtils.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.Sound.*;

@Service
@AllArgsConstructor
public final class ProponerDirectorWebActionHandler implements WebActionHandler<ProponerDirectorWebActionRequestBody> {
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;
    private final UseCaseBus useCaseBus;

    @Override
    public void handle(UUID jugadorId, ProponerDirectorWebActionRequestBody body) throws WebActionException {
        UUID nuevoDirectorId = jugadoresService.getByNombre(body.getNombreDelNuevoDirector()).getJugadorId();
        UUID empresaId = empresasService.getByNombre(body.getNombreDeLaEmpresa()).getEmpresaId();

        useCaseBus.handle(ProponerNuevoDirectorParametros.builder()
                .nuevoDirectorId(nuevoDirectorId)
                .descripccion(body.getDescripccion())
                .jugadorId(jugadorId)
                .empresaId(empresaId)
                .periodoPagoMs(body.getPeriodoPagoEnSegundos())
                .sueldo(body.getSueldo())
                .build());

        enviarMensajeYSonido(jugadorId, GOLD + "Has propuesto como director a " + body.getNombreDelNuevoDirector() +
                " de la empresa " + body.getNombreDeLaEmpresa() + ". Ahora el resto de accionistas tendran que votar a favor. " +
                "Para ver las votaciones " + AQUA + "/empresas votaciones " + body.getNombreDeLaEmpresa(), ENTITY_PLAYER_LEVELUP);
    }
}
