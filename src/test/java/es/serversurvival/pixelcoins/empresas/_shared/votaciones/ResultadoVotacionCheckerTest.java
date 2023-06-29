package es.serversurvival.pixelcoins.empresas._shared.votaciones;

import es.serversurvival._shared.ConfigurationVariables;
import es.serversurvival._shared.TiempoService;
import es.serversurvival._shared.utils.Funciones;
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
    private TiempoService tiempoService;
    private VotosService votosService;

    @BeforeEach
    public void init() {
        this.finalizadorVotacion = mock(FinalizadorVotacion.class);
        this.votacionesService = mock(VotacionesService.class);
        this.empresasService = mock(EmpresasService.class);
        this.tiempoService = mock(TiempoService.class);
        this.votosService = mock(VotosService.class);
        this.resultadoVotacionChecker = new ResultadoVotacionChecker(finalizadorVotacion, votacionesService,
                empresasService, tiempoService, votosService);
    }

    @Test
    public void mayoriaAccionesParticipadas() {
        Empresa empresa = Empresa.builder().nTotalAcciones(100).empresaId(UUID.randomUUID()).build();
        Votacion votacion = CambiarDirectorVotacion.builder().empresaId(empresa.getEmpresaId()).build();

        when(tiempoService.toMillis(votacion.getFechaInicio())).thenReturn(localDateTimeToMillis(votacion.getFechaInicio()));
        when(tiempoService.millis()).thenReturn(System.currentTimeMillis());
        when(empresasService.getById(empresa.getEmpresaId())).thenReturn(empresa);
        when(votosService.getNAccionesVotadas(votacion.getVotacionId())).thenReturn((int) (ConfigurationVariables.EMPRESAS_PORCENTAJE_ACCIONES_TOTALES_VOTACION * empresa.getNTotalAcciones() + 1));

        resultadoVotacionChecker.check(votacion);

        verify(finalizadorVotacion, times(1)).elegirGanadorYFinalizarVotacion(votacion);
    }

    @Test
    public void timeout() {
        Votacion votacion = CambiarDirectorVotacion.builder().build();

        when(tiempoService.toMillis(votacion.getFechaInicio())).thenReturn(localDateTimeToMillis(votacion.getFechaInicio()) - ConfigurationVariables.EMPRESAS_VOTACION_TIME_OUT);
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
