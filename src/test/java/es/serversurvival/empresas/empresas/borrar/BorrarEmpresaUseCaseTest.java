package es.serversurvival.empresas.empresas.borrar;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.empresas.accionistasempresasserver._shared.application.AccionistasServerService;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.AccionistaServer;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static es.serversurvival.MockitoArgEqualsMatcher.of;
import static es.serversurvival.empresas.accionistasserver.AccionistasServerTestMother.*;
import static es.serversurvival.empresas.accionistasserver.AccionistasServerTestMother.createAccionnistaTipoJugaodor;
import static es.serversurvival.empresas.empleados.EmpleadosTestMother.*;
import static es.serversurvival.empresas.empresas.EmpresasTestMother.createEmpresa;
import static es.serversurvival.jugadores.JugadoresTestMother.createJugador;
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
        final int pixelcions = 5;
        Empresa empresaABorrar = createEmpresa("empresa", "jaime", pixelcions)
                .setCotizadaToTrue()
                .withAccionesTotales(accionesTotales);
        when(this.empresasService.getByNombre("empresa")).thenReturn(empresaABorrar);

        AccionistaServer accionionista = createAccionnistaTipoJugaodor("alksj", "empresa", 5);
        AccionistaServer accionionistaEmpresa = createAccionistaTipoEmpresa("jaime", "empresa", 5);
        List<AccionistaServer> accionistas = List.of(accionionista, accionionistaEmpresa);
        when(this.accionistasEmpresasServerService.findByEmpresa("empresa")).thenReturn(accionistas);

        Jugador jugadorAccionista = createJugador("alksj");
        Jugador jugadorOwner = createJugador("jaime");
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(jugadorOwner);
        when(this.jugadoresService.getByNombre("alksj")).thenReturn(jugadorAccionista);

        List<Empleado> empleadosEmpresa = List.of(createEmpleado("pedro", "empresa", 5));
        when(this.empleadosService.findByEmpresa("empresa")).thenReturn(empleadosEmpresa);

        this.useCase.borrar("jaime", "empresa");

        verify(this.jugadoresService, times(1)).save(argThat(of(
                jugadorAccionista.incrementPixelcoinsBy(5 * (pixelcions/accionesTotales))
        )));
        verify(this.jugadoresService, times(1)).save(argThat(of(
                jugadorOwner.incrementPixelcoinsBy(5 * (pixelcions/accionesTotales))
        )));

        verify(this.empresasService, times(1)).deleteByEmpresaId(any());
        verify(this.empleadosService, times(empleadosEmpresa.size())).deleteById(any(UUID.class));
    }

    @Test
    public void shouldBorrarNotCotizada(){
        List<Empleado> empleadosEmpresa = List.of(createEmpleado("pedro", "empresa"), createEmpleado("lkajs", "empresa"));
        Empresa empresaToBorrar = createEmpresa("empresa", "jaime", 10);
        Jugador ownerJugador = createJugador("jaime", 5);
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
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(createJugador("jaime", 1));
        when(this.empleadosService.findByEmpresa("empresa")).thenReturn(Collections.EMPTY_LIST);

        assertThatCode(() -> this.useCase.borrar("jaime", "empresa")).isInstanceOf(NotTheOwner.class);
    }

    @Test
    public void empresaAndJugadorExists() {
        when(this.empresasService.getByNombre("empresa")).thenThrow(ResourceNotFound.class);
        assertThatCode(() -> this.useCase.borrar("jaime", "empresa")).isInstanceOf(ResourceNotFound.class);
    }
}
