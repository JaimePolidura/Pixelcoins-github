package es.serversurvival.v1.empresas.empleados.despedir;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.jaime.javaddd.domain.exceptions.IllegalLength;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
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

import static es.serversurvival.v1.MockitoArgEqualsMatcher.*;
import static es.serversurvival.v1.empresas.empleados.EmpleadosTestMother.createEmpleado;
import static es.serversurvival.v1.empresas.empresas.EmpresasTestMother.createEmpresa;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class DespedirUseCaseTest {
    @Mock private EmpresasService empresasService;
    @Mock private EmpleadosService empleadosService;
    @Mock private EventBus eventBus;
    private DespedirEmpleadoUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new DespedirEmpleadoUseCase(
                this.empresasService,
                this.empleadosService,
                this.eventBus
        );
    }

    @Test
    public void shoulddFire(){
        Empleado empleadoADespedir = createEmpleado("pedro", "empresa");
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "jaime"));
        when(this.empleadosService.getEmpleadoInEmpresa("pedro", "empresa")).thenReturn(empleadoADespedir);

        this.useCase.despedir("jaime", "pedro", "empresa", "holaxd");

        verify(this.empleadosService, times(1)).deleteById(argThat(of(empleadoADespedir.getEmpleadoId())));
        verify(this.eventBus, times(1)).publish(argThat(of(
                EmpleadoDespedido.of("pedro", "empresa", "holaxd")
        )));
    }

    @Test
    public void empleadosWorkdInEmpresa(){
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "jaime"));
        when(this.empleadosService.getEmpleadoInEmpresa("pedro", "empresa")).thenThrow(ResourceNotFound.class);

        assertThatCode(() -> this.useCase.despedir("jaime", "pedro", "empresa", "holaxd"))
                .isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void ownerOfEmpresa(){
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "otro"));
        assertThatCode(() -> this.useCase.despedir("jaime", "pedro", "empresa", "holaxd"))
                .isInstanceOf(NotTheOwner.class);
    }

    @Test
    public void empresaExists(){
        when(this.empresasService.getByNombre("empresa")).thenThrow(ResourceNotFound.class);

        assertThatCode(() -> this.useCase.despedir("jaime", "pedro", "empresa", "holaxd"))
                .isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void razaonCorrectFormat(){
        assertThatCode(() -> this.useCase.despedir("jaime", "pedro", "", null))
                .isInstanceOf(IllegalLength.class);
        assertThatCode(() -> this.useCase.despedir("jaime", "pedro", "",
                "a".repeat(EmpleadosService.MAX_DESPEDIR_RAZON_LENGH + 1)))
                .isInstanceOf(IllegalLength.class);
        assertThatCode(() -> this.useCase.despedir("jaime", "pedro", "",
                "a".repeat(EmpleadosService.MIN_DESPEDIR_RAZON_LENGH - 1)))
                .isInstanceOf(IllegalLength.class);
    }

    @Test
    public void notHisSelf(){
        assertThatCode(() -> this.useCase.despedir("jaime", "jaime", "", ""))
                .isInstanceOf(CannotBeYourself.class);
    }
}
