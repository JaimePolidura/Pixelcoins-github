package es.serversurvival.empresas.empresas.borrar;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.empresas.accionistasempresasserver._shared.application.AccionistasEmpresasServerService;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.AccionEmpresaServer;
import es.serversurvival.empresas.accionistasserver.AccionistasServerTestMother;
import es.serversurvival.empresas.empleados.EmpleadosTestMother;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empresas.EmpresasTestMother;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.jugadores.JugadoresTestMother;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.module.ResolutionException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static es.serversurvival.empresas.accionistasserver.AccionistasServerTestMother.*;
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
    @Mock private AccionistasEmpresasServerService accionistasEmpresasServerService;
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
        List<Empleado> empleadosEmpresa = List.of(createEmpleado("pedro", "empresa"), createEmpleado("lkajs", "empresa"));
        List<AccionEmpresaServer> acciosnistas = List.of(createAccionnistaTipoJugaodor("alksj", "empresa"), createAccionnistaTipoJugaodor("alksasasj", "empresa"));
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("jaime", "jaime").setCotizadaToTrue().withAccionesTotales(10));
        when(this.accionistasEmpresasServerService.findByEmpresa("empresa")).thenReturn(acciosnistas);
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(createJugador("jaime", 1));
        when(this.jugadoresService.getByNombre("alksj")).thenReturn(createJugador("alksj", 1));
        when(this.jugadoresService.getByNombre("alksasasj")).thenReturn(createJugador("alksasasj", 1));
        when(this.empleadosService.findByEmpresa("empresa")).thenReturn(empleadosEmpresa);

        this.useCase.borrar("jaime", "empresa");

        verify(this.jugadoresService, times(acciosnistas.size())).save(any());
        verify(this.empresasService, times(1)).deleteByEmpresaId(any());
        verify(this.empleadosService, times(empleadosEmpresa.size())).deleteById(any(UUID.class));
        verify(this.eventBus, times(1)).publish(any(EmpresaBorradaEvento.class));
    }

    @Test
    public void shouldBorrarNotCotizada(){
        List<Empleado> empleadosEmpresa = List.of(createEmpleado("pedro", "empresa"), createEmpleado("lkajs", "empresa"));
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("jaime", "jaime"));
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(createJugador("jaime", 1));
        when(this.empleadosService.findByEmpresa("empresa")).thenReturn(empleadosEmpresa);

        this.useCase.borrar("jaime", "empresa");

        verify(this.jugadoresService, times(1)).save(any());
        verify(this.empresasService, times(1)).deleteByEmpresaId(any());
        verify(this.empleadosService, times(empleadosEmpresa.size())).deleteById(any(UUID.class));
        verify(this.eventBus, times(1)).publish(any(EmpresaBorradaEvento.class));
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
