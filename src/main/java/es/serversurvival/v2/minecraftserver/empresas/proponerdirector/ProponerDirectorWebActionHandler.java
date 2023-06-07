package es.serversurvival.v2.minecraftserver.empresas.proponerdirector;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v2.minecraftserver.webaction.WebActionException;
import es.serversurvival.v2.minecraftserver.webaction.WebActionHandler;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.v2.pixelcoins.empresas.cambiardirector.proponer.ProponerNuevoDirectorParametros;
import es.serversurvival.v2.pixelcoins.empresas.cambiardirector.proponer.ProponerNuevoDirectorUseCase;
import es.serversurvival.v2.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.AllArgsConstructor;

import java.util.UUID;

import static es.serversurvival.v2.minecraftserver._shared.MinecraftUtils.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.Sound.*;

@Service
@AllArgsConstructor
public final class ProponerDirectorWebActionHandler implements WebActionHandler<ProponerDirectorWebActionRequestBody> {
    private final ProponerNuevoDirectorUseCase proponerNuevoDirectorUseCase;
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;

    @Override
    public void handle(UUID jugadorId, ProponerDirectorWebActionRequestBody body) throws WebActionException {
        UUID nuevoDirectorId = jugadoresService.getByNombre(body.getNuevoDirectorNombre()).getJugadorId();
        UUID empresaId = empresasService.getByNombre(body.getEmpresaNombre()).getEmpresaId();

        proponerNuevoDirectorUseCase.proponer(ProponerNuevoDirectorParametros.builder()
                .nuevoDirectorId(nuevoDirectorId)
                .descripccion(body.getDescripccion())
                .jugadorId(jugadorId)
                .empresaId(empresaId)
                .periodoPagoMs(body.getPeriodoPagoMs())
                .sueldo(body.getSueldo())
                .build());

        enviarMensajeYSonido(jugadorId, GOLD + "Has propuesto como director a " + body.getNuevoDirectorNombre() +
                " de la empresa " + body.getEmpresaNombre() + ". Ahora el resto de accionistas tendran que votar a favor. " +
                "Para ver las votaciones " + AQUA + "/empresas votaciones " + body.getEmpresaNombre(), ENTITY_PLAYER_LEVELUP);
    }
}
