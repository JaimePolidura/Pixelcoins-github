package es.serversurvival.v1.empresas.empleados.irse;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.v1.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.v1.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.v1.empresas.empresas._shared.application.EmpresasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static es.serversurvival.v1.MockitoArgEqualsMatcher.of;
import static es.serversurvival.v1.empresas.empleados.EmpleadosTestMother.createEmpleado;
import static es.serversurvival.v1.empresas.empresas.EmpresasTestMother.createEmpresa;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class IrseUseCaseTest {
    @Mock private EmpresasService empresasService;
    @Mock private EmpleadosService empleadosService;
    @Mock private EventBus eventBus;
    private IrseEmpresaUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new IrseEmpresaUseCase(
                this.empresasService,
                this.empleadosService,
                this.eventBus
        );
    }

    @Test
    public void shouldLeaveEmpresa(){
        Empleado empleado = createEmpleado("jaime", "empresa");
        when(this.empleadosService.getEmpleadoInEmpresa("jaime", "empresa")).thenReturn(empleado);
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "owner"));

        this.useCase.irse("jaime", "empresa");

        verify(this.empleadosService, times(1)).deleteById(argThat(of(empleado.getEmpleadoId())));
        verify(this.eventBus, times(1)).publish(argThat(of(
                EmpleadoDejaEmpresaEvento.of("jaime", "empresa", "owner")
        )));
    }

    @Test
    public void empleadoExists(){
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "owner"));
        when(this.empleadosService.getEmpleadoInEmpresa("jaime", "empresa")).thenThrow(ResourceNotFound.class);
        assertThatCode(() -> this.useCase.irse("jaime", "empresa"))
                .isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void notOwnerOfEmpresa(){
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "jaime"));
        assertThatCode(() -> this.useCase.irse("jaime", "empresa"))
                .isInstanceOf(CannotBeYourself.class);
    }

    @Test
    public void empresaExists(){
        when(this.empresasService.getByNombre("empresa")).thenThrow(ResourceNotFound.class);
        assertThatCode(() -> this.useCase.irse("kljañs", "empresa"))
                .isInstanceOf(ResourceNotFound.class);
    }
}
