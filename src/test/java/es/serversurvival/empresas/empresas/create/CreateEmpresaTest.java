package es.serversurvival.empresas.empresas.create;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.AlreadyExists;
import es.jaime.javaddd.domain.exceptions.IllegalLength;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.empresas.empresas.EmpresasTestMother;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.empresas.empresas.crear.CrearEmpresaUseCase;
import es.serversurvival.empresas.empresas.crear.EmpresaCreadaEvento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static es.serversurvival.empresas.empresas.EmpresasTestMother.createEmpresa;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class CreateEmpresaTest {
    @Mock private EmpresasService empresasService;
    @Mock private EventBus eventBus;
    private CrearEmpresaUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new CrearEmpresaUseCase(this.empresasService, this.eventBus);
    }

    @Test
    public void shouldCreateEmpresa(){
        when(this.empresasService.getByNombre("empresaacalaks")).thenThrow(ResourceNotFound.class);

        this.useCase.crear("jaime", "empresaacalaks", "empresa");

        verify(this.eventBus, times(1)).publish(any(EmpresaCreadaEvento.class));
    }

    @Test
    public void notMaxEmpresasPerJugador(){
        List<Empresa> empresas = IntStream.range(0, EmpresasService.MAX_EMPRESAS_PER_JUGADOR + 1).boxed()
                .map(i -> createEmpresa("jaime", "jaime"))
                .toList();
        when(this.empresasService.getByOwner("jaime")).thenReturn(empresas);

        assertThatCode(() -> this.useCase.crear("jaime", "jaime", "jaime"))
                .isInstanceOf(IllegalQuantity.class);
    }

    @Test
    public void nameNotTaken(){
        when(this.empresasService.getByNombre("empresa1")).thenReturn(createEmpresa("empresa1", "jaime"));

        assertThatCode(() -> this.useCase.crear("jaime", "empresa1", "hola"))
                .isInstanceOf(AlreadyExists.class);
    }

    @Test
    public void descripcionCorrectFormat(){
        assertThatCode(() -> this.useCase.crear("jaime", "empresa", null))
                .isInstanceOf(IllegalLength.class);
        assertThatCode(() -> this.useCase.crear("jaime", "empresa", ""))
                .isInstanceOf(IllegalLength.class);
        assertThatCode(() -> this.useCase.crear("jaime", "empresa", " ".repeat(EmpresasService.MAX_DESC_LONGITUD + 1)))
                .isInstanceOf(IllegalLength.class);
        assertThatCode(() -> this.useCase.crear("empresa", "emoresa", "desc"))
                .isNotInstanceOf(IllegalLength.class);
    }

    @Test
    public void nombreCorrectFormat(){
        assertThatCode(() -> this.useCase.crear("jaime", null, ""))
                .isInstanceOf(IllegalLength.class);
        assertThatCode(() -> this.useCase.crear("jaime", "", ""))
                .isInstanceOf(IllegalLength.class);
        assertThatCode(() -> this.useCase.crear("jaime", "".repeat(EmpresasService.MAX_NOMBRE_LONGITUD + 1), ""))
                .isInstanceOf(IllegalLength.class);
        assertThatCode(() -> this.useCase.crear("empresa", "emoresa", "desc"))
                .isNotInstanceOf(IllegalLength.class);
    }
}
