package es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.domain.AccionistaEmpresa;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.application.VotosService;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.application.VotacionesService;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.domain.Voto;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public final class DecrementorPosicionesAccionesJugador {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final VotacionesService votacionesService;
    private final VotosService votosService;

    public void decrementarEnUno(UUID jugadorId, UUID empresaId) {
        AccionistaEmpresa accionistaServer = accionistasEmpresasService.getByEmpresaIdAndJugadorId(empresaId, jugadorId)
                .decrementarNAccionesPorUno();

        if(accionistaServer.noTieneMasAcciones()){
            accionistasEmpresasService.deleteById(accionistaServer.getAccionistaId());
            borrarVotos(jugadorId, empresaId);
        }else{
            accionistasEmpresasService.save(accionistaServer);
            decrementarVotosEnUno(jugadorId, empresaId);
        }
    }

    private void decrementarVotosEnUno(UUID jugadorId, UUID empresaId) {
        getVotosVotadosPorElJugador(jugadorId, empresaId)
                .forEach(voto -> votosService.save(voto.decrementarNAccionesEnUno()));
    }

    private void borrarVotos(UUID jugadorId, UUID empresaId) {
        getVotosVotadosPorElJugador(jugadorId, empresaId)
                .forEach(voto -> votosService.deleteById(voto.getVotoId()));
    }

    private Stream<Voto> getVotosVotadosPorElJugador(UUID jugadorId, UUID empresaId) {
        return votacionesService.findByEmpresaId(empresaId).stream()
                .map(votacion -> votosService.findByVotacionId(votacion.getVotacionId()))
                .flatMap(Collection::stream)
                .filter(voto -> voto.getJugadorId().equals(jugadorId));
    }
}
