package es.serversurvival.pixelcoins.empresas._shared.votaciones;

import es.serversurvival._shared.ConfigurationVariables;
import es.serversurvival._shared.TiempoService;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.config._shared.application.Configuration;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationKey;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.application.VotacionesService;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.Votacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.application.VotosService;
import es.serversurvival.pixelcoins.empresas.cambiardirector.CambiarDirectorVotacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ResultadoVotacionCheckerTest {
    private ResultadoVotacionChecker resultadoVotacionChecker;
    private FinalizadorVotacion finalizadorVotacion;
    private VotacionesService votacionesService;
    private EmpresasService empresasService;
    private Configuration configuration;
    private TiempoService tiempoService;
    private VotosService votosService;

    @BeforeEach
    public void init() {
        this.finalizadorVotacion = mock(FinalizadorVotacion.class);
        this.votacionesService = mock(VotacionesService.class);
        this.empresasService = mock(EmpresasService.class);
        this.tiempoService = mock(TiempoService.class);
        this.configuration = mock(Configuration.class);
        this.votosService = mock(VotosService.class);
        this.resultadoVotacionChecker = new ResultadoVotacionChecker(finalizadorVotacion, votacionesService,
                empresasService, configuration, tiempoService, votosService);
    }

    @Test
    public void mayoriaAccionesParticipadas() {
        Empresa empresa = Empresa.builder().nTotalAcciones(100).empresaId(UUID.randomUUID()).build();
        Votacion votacion = CambiarDirectorVotacion.builder().empresaId(empresa.getEmpresaId()).build();
        double minimoPorcenatajeVotosAcciones = 0.8;

        when(configuration.getLong(ConfigurationKey.EMPRESAS_VOTACION_TIME_OUT)).thenReturn((long) (24 * 60 * 60 * 1000));
        when(configuration.getDouble(ConfigurationKey.EMPRESAS_PORCENTAJE_ACCIONES_TOTALES_VOTACION)).thenReturn(minimoPorcenatajeVotosAcciones);

        when(tiempoService.toMillis(votacion.getFechaInicio())).thenReturn(localDateTimeToMillis(votacion.getFechaInicio()));
        when(tiempoService.millis()).thenReturn(System.currentTimeMillis());
        when(empresasService.getById(empresa.getEmpresaId())).thenReturn(empresa);
        when(votosService.getNAccionesVotadas(votacion.getVotacionId())).thenReturn((int) (minimoPorcenatajeVotosAcciones * empresa.getNTotalAcciones() + 1));

        resultadoVotacionChecker.check(votacion);

        verify(finalizadorVotacion, times(1)).elegirGanadorYFinalizarVotacion(votacion);
    }

    @Test
    public void timeout() {
        Votacion votacion = CambiarDirectorVotacion.builder().build();
        long votacionTimeout = (24 * 60 * 60 * 1000);

        when(configuration.getLong(ConfigurationKey.EMPRESAS_VOTACION_TIME_OUT)).thenReturn(votacionTimeout);
        when(configuration.getDouble(ConfigurationKey.EMPRESAS_PORCENTAJE_ACCIONES_TOTALES_VOTACION)).thenReturn(0.8);
        when(tiempoService.toMillis(votacion.getFechaInicio())).thenReturn(localDateTimeToMillis(votacion.getFechaInicio()) - votacionTimeout);
        when(tiempoService.millis()).thenReturn(System.currentTimeMillis());

        resultadoVotacionChecker.check(votacion);

        verify(finalizadorVotacion, times(1)).elegirGanadorYFinalizarVotacion(votacion);
    }

    private long localDateTimeToMillis(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);

        return zonedDateTime.toInstant().toEpochMilli();
    }
}
