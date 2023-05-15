package es.serversurvival.v1.empresas.empresas.borrar;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.v1.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.v1.empresas.accionistasserver._shared.domain.AccionistaServer;
import es.serversurvival.v1.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.v1.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.v1.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.v1.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import es.serversurvival.v1.empresas.accionistasserver.AccionistasServerTestMother;
import es.serversurvival.v1.empresas.empleados.EmpleadosTestMother;
import es.serversurvival.v1.jugadores.JugadoresTestMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static es.serversurvival.v1.MockitoArgEqualsMatcher.of;
import static es.serversurvival.v1.empresas.accionistasserver.AccionistasServerTestMother.createAccionnistaTipoJugaodor;
import static es.serversurvival.v1.empresas.empresas.EmpresasTestMother.createEmpresa;
import static es.serversurvival.v1.jugadores.JugadoresTestMother.createJugador;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class BorrarEmpresaUseCaseTest {
    @Mock private EmpresasService empresasService;
    @Mock private JugadoresService jugadoresService;
    @Mock private EmpleadosService empleadosService;
    @Mock private AccionistasServerService accionistasEmpresasServerService;
    @Mock private EventBus eventBus;
    private BorrarEmpresaUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new BorrarEmpresaUseCase(
                this.empresasService,
                this.jugadoresService,
                this.empleadosService,
                this.accionistasEmpresasServerService,
                this.eventBus
        );
    }

    @Test
    public void shouldBorrarCotizada(){
        final int accionesTotales = 10;
        final double pixelcions = 100;
        final String empresaNombre = "empresa";
        Empresa empresaABorrar = createEmpresa(empresaNombre, "jaime", pixelcions)
                .setCotizadaToTrue()
                .withAccionesTotales(accionesTotales);
        when(this.empresasService.getByNombre(empresaNombre)).thenReturn(empresaABorrar);

        AccionistaServer accionistaJugadorOwner = AccionistasServerTestMother.createAccionnistaTipoJugaodor("jaime", empresaNombre, 3);
        AccionistaServer accionistaJugadorOtro = AccionistasServerTestMother.createAccionnistaTipoJugaodor("otro", empresaNombre, 2);
        AccionistaServer accionionistaEmpresa = AccionistasServerTestMother.createAccionistaTipoEmpresa(empresaNombre, empresaNombre, 5);

        List<AccionistaServer> accionistas = List.of(accionistaJugadorOwner, accionionistaEmpresa, accionistaJugadorOtro);
        when(this.accionistasEmpresasServerService.findByEmpresaTipoJugador(empresaNombre))
                .thenReturn(List.of(accionistaJugadorOwner, accionistaJugadorOtro));
        when(this.accionistasEmpresasServerService.findByEmpresaTipoEmpresa(empresaNombre))
                .thenReturn(List.of(accionionistaEmpresa));

        Jugador jugadorOtro = JugadoresTestMother.createJugador("otro");
        Jugador jugadorOwner = JugadoresTestMother.createJugador("jaime");
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(jugadorOwner);
        when(this.jugadoresService.getByNombre("otro")).thenReturn(jugadorOtro);

        List<Empleado> empleadosEmpresa = List.of(EmpleadosTestMother.createEmpleado("pedro", empresaNombre, 5));
        when(this.empleadosService.findByEmpresa(empresaNombre)).thenReturn(empleadosEmpresa);

        this.useCase.borrar("jaime", empresaNombre);

        double ownerShiptJugadorOtro = 2 / (double) (10 - 5);
        double ownerShiptJugadorOwner = 3 / (double) (10 - 5);
        verify(this.jugadoresService, times(1)).save(argThat(of(
                jugadorOwner.incrementPixelcoinsBy(pixelcions * ownerShiptJugadorOwner)
        )));
        verify(this.jugadoresService, times(1)).save(argThat(of(
                jugadorOtro.incrementPixelcoinsBy(pixelcions * ownerShiptJugadorOtro)
        )));

        verify(this.empresasService, times(1)).deleteByEmpresaId(any());
        verify(this.empleadosService, times(empleadosEmpresa.size())).deleteById(any(UUID.class));
    }

    @Test
    public void shouldBorrarNotCotizada(){
        List<Empleado> empleadosEmpresa = List.of(EmpleadosTestMother.createEmpleado("pedro", "empresa"), EmpleadosTestMother.createEmpleado("lkajs", "empresa"));
        Empresa empresaToBorrar = createEmpresa("empresa", "jaime", 10);
        Jugador ownerJugador = JugadoresTestMother.createJugador("jaime", 5);
        when(this.empresasService.getByNombre("empresa")).thenReturn(empresaToBorrar);
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(ownerJugador);
        when(this.empleadosService.findByEmpresa("empresa")).thenReturn(empleadosEmpresa);

        this.useCase.borrar("jaime", "empresa");

        verify(this.jugadoresService, times(1)).save(argThat(of(
                ownerJugador.incrementPixelcoinsBy(10)
        )));
        verify(this.empresasService, times(1)).deleteByEmpresaId(any());
        verify(this.empleadosService, times(empleadosEmpresa.size())).deleteById(any(UUID.class));

        verify(this.eventBus, times(1)).publish(argThat(of(
                EmpresaBorrada.of(ownerJugador.getNombre(), empresaToBorrar.getNombre(), 10, empleadosEmpresa.stream()
                        .map(Empleado::getNombre).toList()
                )
        )));
    }

    @Test
    public void ownerOfEmpresa(){
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("jaime", "otro"));
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(JugadoresTestMother.createJugador("jaime", 1));
        when(this.empleadosService.findByEmpresa("empresa")).thenReturn(Collections.EMPTY_LIST);

        assertThatCode(() -> this.useCase.borrar("jaime", "empresa")).isInstanceOf(NotTheOwner.class);
    }

    @Test
    public void empresaAndJugadorExists() {
        when(this.empresasService.getByNombre("empresa")).thenThrow(ResourceNotFound.class);
        assertThatCode(() -> this.useCase.borrar("jaime", "empresa")).isInstanceOf(ResourceNotFound.class);
    }
}
