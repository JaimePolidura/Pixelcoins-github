package es.serversurvival.empresas.empresas.vender;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.empresas.empleados.EmpleadosTestMother;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static es.serversurvival.empresas.empleados.EmpleadosTestMother.createEmpleado;
import static es.serversurvival.empresas.empresas.EmpresasTestMother.createEmpresa;
import static es.serversurvival.jugadores.JugadoresTestMother.createJugador;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class VenderEmpresaUseCaseTest {
    @Mock private EmpresasService empresasService;
    @Mock private JugadoresService jugadoresService;
    @Mock private EmpleadosService empleadosService;
    @Mock private EventBus eventBus;
    private VenderEmpresaUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new VenderEmpresaUseCase(
                this.empresasService,
                this.jugadoresService,
                this.empleadosService,
                this.eventBus
        );
    }

    @Test
    public void shouldVenderWithCompradorEmployee(){
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "vendedor"));
        when(this.jugadoresService.getByNombre("vendedor")).thenReturn(createJugador("vendedor", 100));
        when(this.jugadoresService.getByNombre("comprador")).thenReturn(createJugador("comprador", 100));
        when(this.empleadosService.findByEmpresa("empresa")).thenReturn(List.of(createEmpleado("comprador", "empresa")));

        this.useCase.vender("vendedor", "comprador", 5, "empresa");

        verify(this.empresasService, times(1)).save(any(Empresa.class));
        verify(this.jugadoresService, times(1)).realizarTransferenciaConEstadisticas(any(), any(), anyDouble());
        verify(this.eventBus, times(1)).publish(any(EmpresaVendedida.class));
        verify(this.empleadosService, times(1)).deleteById(any(UUID.class));
    }

    @Test
    public void shouldVender(){
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "vendedor"));
        when(this.jugadoresService.getByNombre("vendedor")).thenReturn(createJugador("vendedor", 100));
        when(this.jugadoresService.getByNombre("comprador")).thenReturn(createJugador("comprador", 100));

        this.useCase.vender("vendedor", "comprador", 5, "empresa");

        verify(this.empresasService, times(1)).save(any(Empresa.class));
        verify(this.jugadoresService, times(1)).realizarTransferenciaConEstadisticas(any(), any(), anyDouble());
        verify(this.eventBus, times(1)).publish(any(EmpresaVendedida.class));
    }

    @Test
    public void enoughPixelcoins(){
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "vendedor"));
        when(this.jugadoresService.getByNombre("vendedor")).thenReturn(createJugador("vendedor", 100));
        when(this.jugadoresService.getByNombre("comprador")).thenReturn(createJugador("comprador", 1));

        assertThatCode(() -> this.useCase.vender("vendedor", "comprador", 5, "empresa"))
                .isInstanceOf(NotEnoughPixelcoins.class);
    }

    @Test
    public void ownerOfEmpresa(){
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "otro"));

        assertThatCode(() -> this.useCase.vender("jaime", "empresa", 5, "empresa"))
                .isInstanceOf(NotTheOwner.class);
    }

    @Test
    public void empresaExists(){
        when(this.empresasService.getByNombre("empresaFail")).thenThrow(ResourceNotFound.class);

        assertThatCode(() -> this.useCase.vender("jaime", "empresaFail", 5, "empresaFail"))
                .isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void correctFormatPixelcoins(){
        assertThatCode(() -> this.useCase.vender("", "", -1, "")).isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.vender("", "", 0, "")).isInstanceOf(IllegalQuantity.class);
    }
}
