package es.serversurvival.pixelcoins.empresas._shared.votaciones;

import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.*;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.VotosService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class FinalizadorVotacion {
    private final VotacionFinalizadoCaller votacionFinalizadoCaller;
    private final VotacionesService votacionesService;
    private final VotosService votosService;

    public void elegirGanadorYFinalizarVotacion(Votacion votacion) {
        int votosFavor = votosService.getVotosFavor(votacion.getVotacionId());
        int votosContra = votosService.getVotosContra(votacion.getVotacionId());

        EstadoVotacion resultado = getResultadoVotacion(votosFavor, votosContra);

        votacionesService.save(votacion.finalizar(resultado));

        votacionFinalizadoCaller.call(votacion, ResultadoVotacion.fromEstadoVotacion(resultado));
    }

    private EstadoVotacion getResultadoVotacion(int votosFavor, int votosContra) {
        if(votosFavor > votosContra){
            return EstadoVotacion.ACEPTADO;
        }else if (votosFavor < votosContra) {
            return EstadoVotacion.RECHAZADO;
        }else{
            return EstadoVotacion.EMPATE;
        }
    }
}
