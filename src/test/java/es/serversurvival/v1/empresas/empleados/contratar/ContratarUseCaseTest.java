package es.serversurvival.v1.empresas.empleados.contratar;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.*;
import es.serversurvival.v1.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.v1.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.v1.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.v1.empresas.empleados._shared.domain.TipoSueldo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static es.serversurvival.v1.MockitoArgEqualsMatcher.of;
import static es.serversurvival.v1.empresas.empleados.EmpleadosTestMother.createEmpleado;
import static es.serversurvival.v1.empresas.empresas.EmpresasTestMother.createEmpresa;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ContratarUseCaseTest {
    @Mock private EmpleadosService empleadosService;
    @Mock private EmpresasService empresasService;
    @Mock private EventBus eventBus;
    private ContratarUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new ContratarUseCase(
                this.empleadosService,
                this.empresasService,
                this.eventBus
        );
    }

    @Test
    public void shouldContratar(){
        Empresa empresa = createEmpresa("empresa", "jaime");
        when(this.empresasService.getByNombre("empresa")).thenReturn(empresa);
        when(this.empleadosService.findByEmpresa("empresa")).thenReturn(List.of(
                createEmpleado("otro", "empresa")
        ));

        this.useCase.contratar("jaime", "pedro", "empresa", 2, TipoSueldo.DIA, "trabajador");

        verify(this.empleadosService, times(1)).save(
                argThat(of("pedro")), argThat(of("empresa")), anyDouble(), argThat(of(TipoSueldo.DIA)), argThat(of("trabajador"))
        );
        verify(this.eventBus, times(1)).publish(
                JugadorContratado.of("pedro", "empresa", "trabajador")
        );
    }

    @Test
    public void employeeToHireNotAlreadyWorks(){
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "jaime"));
        when(this.empleadosService.findByEmpresa("empresa")).thenReturn(List.of(
                createEmpleado("otro", "empresa"),
                createEmpleado("pedro", "empresa")
        ));

        assertThatCode(() -> this.useCase.contratar("jaime", "pedro", "empresa", 2, TipoSueldo.DIA, "trabajador"))
                .isInstanceOf(AlreadyExists.class);
    }

    @Test
    public void ownerOfEmpresa(){
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "otro"));
        assertThatCode(() -> this.useCase.contratar("jaime", "pedro", "empresa", 2, TipoSueldo.DIA, "trabajador"))
                .isInstanceOf(NotTheOwner.class);
    }

    @Test
    public void empresaExists(){
        when(this.empresasService.getByNombre("empresa")).thenThrow(ResourceNotFound.class);
        assertThatCode(() -> this.useCase.contratar("jaime", "pedro", "empresa", 2, TipoSueldo.DIA, "trabajador"))
                .isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void correctFormatSueldo(){
        assertThatCode(() -> this.useCase.contratar("jaime", "pedro", "", 0, TipoSueldo.DIA, "trabajador"))
                .isInstanceOf(IllegalQuantity.class);

        assertThatCode(() -> this.useCase.contratar("jaime", "pedro", "", -1, TipoSueldo.DIA, "trabajador"))
                .isInstanceOf(IllegalQuantity.class);
    }

    @Test
    public void tipoSueldoCorrectFormat(){
        assertThatCode(() -> this.useCase.contratar("jaime", "pedro", "", 0, null, "trabajador"))
                .isInstanceOf(IllegalQuantity.class);
    }

    @Test
    public void cargoCorrectFormat(){
        assertThatCode(() -> this.useCase.contratar("jaime", "pedro", "", 0, null, null))
                .isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.contratar("jaime", "pedro", "", 0, null,
                "a".repeat(EmpleadosService.MAX_CARGO_LENGTH + 1)))
                .isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.contratar("jaime", "pedro", "", 0, null,
                "a".repeat(EmpleadosService.MIN_CARGO_LENGTH - 1)))
                .isInstanceOf(IllegalQuantity.class);
    }

    @Test
    public void notHisSelf(){
        assertThatCode(() -> this.useCase.contratar("jaime", "jaime", "", 10, null, null))
                .isInstanceOf(CannotBeYourself.class);
    }
}
