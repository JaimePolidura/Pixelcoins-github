package es.serversurvival.empresas.empresas.pagarsueldos;

import es.jaime.EventBus;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static es.serversurvival.empresas.empleados.EmpleadosTestMother.*;
import static es.serversurvival.empresas.empresas.EmpresasTestMother.*;
import static es.serversurvival.jugadores.JugadoresTestMother.createJugador;
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
        Empresa empresa = createEmpresa("empresa", "jaime", 500);

        when(this.empresasService.getByNombre("empresa")).thenReturn(empresa);
        when(this.jugadoresService.getByNombre("pedro2")).thenReturn(createJugador("pedro2"));
        when(this.jugadoresService.getByNombre("pedro3")).thenReturn(createJugador("pedro3"));
        when(this.jugadoresService.getByNombre("pedro4")).thenReturn(createJugador("pedro4"));
        //Only one player cannot get paid bcs he got paid recently
        List<Empleado> listEmpleados = List.of(
                createEmpleado("pedro1", "empresa", 10, TipoSueldo.DIA, Funciones.DATE_FORMATER_LEGACY.format(new Date())),
                createEmpleado("pedro2", "empresa", 10, TipoSueldo.SEMANA, "2020-02-11"),
                createEmpleado("pedro3", "empresa", 10, TipoSueldo.SEMANA_2, "1999-02-11"),
                createEmpleado("pedro4", "empresa", 10, TipoSueldo.MES, "1999-02-11")
        );

        this.useCase.pagarSueldos(empresa, listEmpleados);

        verify(this.eventBus, times(listEmpleados.size() - 1)).publish(any(SueldoPagadoEvento.class));
        verify(this.empresasService, times(listEmpleados.size() - 1)).save(any(Empresa.class));
        verify(this.jugadoresService, times(listEmpleados.size() - 1)).save(any(Jugador.class));
        verify(this.empleadosService, times(listEmpleados.size() - 1)).save(any(Empleado.class));
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
