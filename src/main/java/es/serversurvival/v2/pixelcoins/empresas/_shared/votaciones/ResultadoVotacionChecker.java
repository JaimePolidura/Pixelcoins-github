package es.serversurvival.v2.pixelcoins.empresas._shared.votaciones;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.votaciones.*;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones._shared.votos.VotosService;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static es.serversurvival.v1._shared.utils.Funciones.*;

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
        int accionesVotadas = votosService.getNumeroAccionesVotadas(votacion.getVotacionId());
        double porcentajeVotosSobreAccionEmpresa = (double) accionesVotadas / empresa.getNTotalAcciones();

        if(porcentajeVotosSobreAccionEmpresa >= Votacion.DEFAULT_PORCENTAJE_ACCIONES_TOTALES_VOTACION) {
            finalizadorVotacion.elegirGanadorYFinalizarVotacion(votacion);
        }
    }
}
