package es.serversurvival.empresas.empresas.pagarsueldos;

import es.jaime.EventBus;
import es.serversurvival.MockitoArgEqualsMatcher;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.empresas.empleados.EmpleadosTestMother;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empleados._shared.domain.TipoSueldo;
import es.serversurvival.empresas.empresas.EmpresasTestMother;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.empresas.empresas.pagarsueldostask.ErrorPagandoSueldo;
import es.serversurvival.empresas.empresas.pagarsueldostask.PagarSueldosUseCase;
import es.serversurvival.empresas.empresas.pagarsueldostask.SueldoPagadoEvento;
import es.serversurvival.jugadores.JugadoresTestMother;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static es.serversurvival.MockitoArgEqualsMatcher.of;
import static es.serversurvival._shared.utils.Funciones.hoy;
import static es.serversurvival.empresas.empleados.EmpleadosTestMother.*;
import static es.serversurvival.empresas.empresas.EmpresasTestMother.*;
import static es.serversurvival.jugadores.JugadoresTestMother.createJugador;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class PagarSueldosTest {
    @Mock private EmpresasService empresasService;
    @Mock private JugadoresService jugadoresService;
    @Mock private EmpleadosService empleadosService;
    @Mock private EventBus eventBus;
    private PagarSueldosUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new PagarSueldosUseCase(
                this.empresasService,
                this.jugadoresService,
                this.empleadosService,
                this.eventBus
        );
    }

    @Test
    public void shouldMakePayment(){
        final double SUELDO = 10;
        final double INITIAL_EMPLEADOS_PIXELCOINS = 100;
        final double INITIAL_EMPRESAS_PIXELCOINS = 500;
        Empresa empresa = createEmpresa("empresa", "jaime", INITIAL_EMPRESAS_PIXELCOINS);

        Empleado empleado1 = createEmpleado("pedro1", "empresa", SUELDO, TipoSueldo.DIA, "2020-02-11");
        Jugador empleado1Jugador = createJugador("pedro1", INITIAL_EMPLEADOS_PIXELCOINS);
        Empleado empleado2 = createEmpleado("pedro2", "empresa", SUELDO, TipoSueldo.SEMANA, "2020-02-11");
        Jugador empleado2Jugador = createJugador("pedro2", INITIAL_EMPLEADOS_PIXELCOINS);
        Empleado empleado3 = createEmpleado("pedro3", "empresa", SUELDO, TipoSueldo.SEMANA_2, "1999-02-11");
        Jugador empleado3Jugador = createJugador("pedro3", INITIAL_EMPLEADOS_PIXELCOINS);

        when(this.empresasService.getByNombre("empresa")).thenReturn(empresa);
        when(this.jugadoresService.getByNombre("pedro1")).thenReturn(empleado1Jugador);
        when(this.jugadoresService.getByNombre("pedro2")).thenReturn(empleado2Jugador);
        when(this.jugadoresService.getByNombre("pedro3")).thenReturn(empleado3Jugador);
        //Only one player cannot get paid bcs he got paid recently
        List<Empleado> listEmpleados = List.of(empleado1, empleado2, empleado3, createEmpleado("pedro4", "empresa", 10, TipoSueldo.MES, hoy()));

        this.useCase.pagarSueldos(empresa, listEmpleados);

        ArgumentCaptor<Jugador> argumentJugadorCapturer = ArgumentCaptor.forClass(Jugador.class);
        verify(this.jugadoresService, times(listEmpleados.size() - 1)).save(argumentJugadorCapturer.capture());
        var allJugadoresHaveBeenPaid = argumentJugadorCapturer.getAllValues().stream()
                .allMatch(j -> j.getPixelcoins() - SUELDO == INITIAL_EMPLEADOS_PIXELCOINS && j.getIngresos() - SUELDO == 0);
        assertThat(allJugadoresHaveBeenPaid).isTrue();

        ArgumentCaptor<SueldoPagadoEvento> argumentEventoJugadorPagandoCapturer = ArgumentCaptor.forClass(SueldoPagadoEvento.class);
        verify(this.eventBus, times(listEmpleados.size() - 1)).publish(argumentEventoJugadorPagandoCapturer.capture());
        var allEventsCorrectlyThrown = argumentEventoJugadorPagandoCapturer.getAllValues().stream()
                        .allMatch(e -> e.getEmpresa().equalsIgnoreCase(empresa.getNombre()) && e.getSueldo() == SUELDO);
        assertThat(allEventsCorrectlyThrown).isTrue();

        ArgumentCaptor<Empleado> argumentEmpleadoCapturer = ArgumentCaptor.forClass(Empleado.class);
        verify(this.empleadosService, times(listEmpleados.size() - 1)).save(argumentEmpleadoCapturer.capture());
        var allEmpleadosSavedMatches = argumentEmpleadoCapturer.getAllValues().stream()
                        .allMatch(e -> e.getFechaUltimaPaga().equalsIgnoreCase(Funciones.hoy()));
        assertThat(allEmpleadosSavedMatches).isTrue();
    }

    @Test
    public void noPixelcoins(){
        Empresa empresa = createEmpresa("empresa", "jaime", 5);

        when(this.empresasService.getByNombre("empresa")).thenReturn(empresa);
        List<Empleado> listEmpleados = List.of(
                createEmpleado("pedor", "empresa", 10, TipoSueldo.DIA, "1999-02-11"),
                createEmpleado("pedor", "empresa", 10, TipoSueldo.SEMANA, "2020-02-11"),
                createEmpleado("pedor", "empresa", 10, TipoSueldo.SEMANA_2, "1999-02-11"),
                createEmpleado("pedor", "empresa", 10, TipoSueldo.MES, "1999-02-11")
        );

        this.useCase.pagarSueldos(empresa, listEmpleados);

        verify(this.eventBus, times(listEmpleados.size())).publish(any(ErrorPagandoSueldo.class));
    }
}
