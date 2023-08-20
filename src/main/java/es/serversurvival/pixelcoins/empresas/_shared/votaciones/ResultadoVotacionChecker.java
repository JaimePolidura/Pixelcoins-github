package es.serversurvival.pixelcoins.empresas._shared.votaciones;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival._shared.ConfigurationVariables;
import es.serversurvival._shared.TiempoService;
import es.serversurvival.pixelcoins.config._shared.application.Configuration;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationKey;
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
public class ResultadoVotacionChecker {
    private final FinalizadorVotacion finalizadorVotacion;
    private final VotacionesService votacionesService;
    private final EmpresasService empresasService;
    private final Configuration configuration;
    private final TiempoService tiempoService;
    private final VotosService votosService;

    public void check(UUID votacionId) {
        check(votacionesService.getById(votacionId));
    }

    public void check(Votacion votacion) {
        long tiempoTranscurridoDesdeInicioVotacion = tiempoService.millis() - tiempoService.toMillis(votacion.getFechaInicio());
        long votacioneTimtout = configuration.getLong(ConfigurationKey.EMPRESAS_VOTACION_TIME_OUT);

        if(tiempoTranscurridoDesdeInicioVotacion >= votacioneTimtout){
            finalizadorVotacion.elegirGanadorYFinalizarVotacion(votacion);
            return;
        }

        Empresa empresa = empresasService.getById(votacion.getEmpresaId());
        int accionesVotadas = votosService.getNAccionesVotadas(votacion.getVotacionId());
        double porcentajeActualVotosSobreAccionEmpresa = (double) accionesVotadas / empresa.getNTotalAcciones();
        double porcentajeMinimoVotosSobreAccionEmpresa = configuration.getDouble(ConfigurationKey.EMPRESAS_PORCENTAJE_ACCIONES_TOTALES_VOTACION);

        if(porcentajeActualVotosSobreAccionEmpresa > porcentajeMinimoVotosSobreAccionEmpresa) {
            finalizadorVotacion.elegirGanadorYFinalizarVotacion(votacion);
        }
    }
}
