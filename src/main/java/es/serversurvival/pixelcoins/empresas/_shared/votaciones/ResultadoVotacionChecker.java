package es.serversurvival.pixelcoins.empresas._shared.votaciones;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.Votacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.application.VotosService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.application.VotacionesService;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.*;

@Service
@AllArgsConstructor
public final class ResultadoVotacionChecker {
    private final FinalizadorVotacion finalizadorVotacion;
    private final VotacionesService votacionesService;
    private final EmpresasService empresasService;
    private final VotosService votosService;

    public void check(UUID votacionId) {
        check(votacionesService.getById(votacionId));
    }

    public void check(Votacion votacion) {
        long tiempoTranscurridoDesdeInicioVotacion = toMillis(LocalDateTime.now()) - toMillis(votacion.getFechaInicio());

        if(tiempoTranscurridoDesdeInicioVotacion >= Votacion.DEFAULT_VOTACION_TIME_OUT){
            finalizadorVotacion.elegirGanadorYFinalizarVotacion(votacion);
            return;
        }

        Empresa empresa = empresasService.getById(votacion.getEmpresaId());
        int accionesVotadas = votosService.getNAccionesVotadas(votacion.getVotacionId());
        double porcentajeVotosSobreAccionEmpresa = (double) accionesVotadas / empresa.getNTotalAcciones();

        if(porcentajeVotosSobreAccionEmpresa >= Votacion.DEFAULT_PORCENTAJE_ACCIONES_TOTALES_VOTACION) {
            finalizadorVotacion.elegirGanadorYFinalizarVotacion(votacion);
        }
    }
}
