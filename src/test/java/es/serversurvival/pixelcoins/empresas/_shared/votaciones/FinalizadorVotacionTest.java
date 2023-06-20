package es.serversurvival.pixelcoins.empresas._shared.votaciones;

import es.serversurvival.ArgPredicateMatcher;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.application.VotacionFinalizadoCaller;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.application.VotacionesService;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.EstadoVotacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.ResultadoVotacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.Votacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.application.VotosService;
import es.serversurvival.pixelcoins.empresas.cambiardirector.CambiarDirectorVotacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class FinalizadorVotacionTest {
    private FinalizadorVotacion toTest;
    private VotacionFinalizadoCaller votacionFinalizadoCaller;
    private VotacionesService votacionesService;
    private VotosService votosService;

    @BeforeEach
    public void init() {
        this.votacionFinalizadoCaller = mock(VotacionFinalizadoCaller.class);
        this.votacionesService = mock(VotacionesService.class);
        this.votosService = mock(VotosService.class);

        this.toTest = new FinalizadorVotacion(votacionFinalizadoCaller, votacionesService, votosService);
    }

    @Test
    public void rechazado() {
        testearVotacion(40, 50, EstadoVotacion.RECHAZADO);
    }

    @Test
    public void aceptado() {
        testearVotacion(50, 40, EstadoVotacion.ACEPTADO);
    }

    @Test
    public void empate() {
        testearVotacion(50, 50, EstadoVotacion.EMPATE);
    }

    private void testearVotacion(int t, int t1, EstadoVotacion empate) {
        Votacion votacion = CambiarDirectorVotacion.builder().build();

        when(votosService.getVotosFavor(votacion.getVotacionId())).thenReturn(t);
        when(votosService.getVotosContra(votacion.getVotacionId())).thenReturn(t1);

        toTest.elegirGanadorYFinalizarVotacion(votacion);

        verify(votacionesService, times(1)).save(argThat(ArgPredicateMatcher.of(it -> it.getEstado() == empate)));
        verify(votacionFinalizadoCaller, times(1)).call(votacion, ResultadoVotacion.fromEstadoVotacion(empate));
    }
}
