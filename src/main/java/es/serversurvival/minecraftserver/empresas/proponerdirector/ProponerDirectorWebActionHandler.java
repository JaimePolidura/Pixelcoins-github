package es.serversurvival.minecraftserver.empresas.proponerdirector;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver.webaction.WebActionException;
import es.serversurvival.minecraftserver.webaction.WebActionHandler;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas.cambiardirector.proponer.ProponerNuevoDirectorParametros;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class ProponerDirectorWebActionHandler implements WebActionHandler<ProponerDirectorWebActionRequestBody> {
    private final EnviadorMensajes enviadorMensajes;
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
                .periodoPagoMs(Funciones.diasToMillis(body.getPeriodoPagoEnDias()))
                .sueldo(body.getSueldo())
                .build());
    }
}
