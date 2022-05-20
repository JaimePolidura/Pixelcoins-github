package es.serversurvival.empresas.empresas.editarnombre;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.AlreadyExists;
import es.jaime.javaddd.domain.exceptions.IllegalLength;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.MockitoArgEqualsMatcher;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.empresas.empresas.editardescripccion.EditarDescUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static es.serversurvival.MockitoArgEqualsMatcher.*;
import static es.serversurvival.empresas.empresas.EmpresasTestMother.createEmpresa;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class EditarNombreUseCaseTest {
    @Mock private EmpresasService empresasService;
    @Mock private EventBus eventBus;
    private EditarNombreUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new EditarNombreUseCase(this.empresasService, this.eventBus);
    }

    @Test
    public void shouldEditName(){
        Empresa empresaToEdit = createEmpresa("empresa", "jaime");
        when(this.empresasService.getByNombre("empresa")).thenReturn(empresaToEdit);

        this.useCase.edit("empresa", "gola", "jaime");

        verify(this.empresasService).save(argThat(of(empresaToEdit.withNombre("gola"))));

        verify(this.eventBus).publish(argThat(of(EmpresaNombreEditadoEvento.of("empresa", "gola"))));
    }

    @Test
    public void ownerOfEmpresa(){
        when(this.empresasService.getByNombre("antiguoNombre")).thenReturn(createEmpresa("antiguoNombre", "otro"));

        assertThatCode(() -> this.useCase.edit("antiguoNombre", "gola", "jaime"))
                .isInstanceOf(NotTheOwner.class);
    }

    @Test
    public void ensureNameNotTaken(){
        when(this.empresasService.getByNombre("nuevoNombre")).thenReturn(createEmpresa("nuevoNombre", "otro"));

        assertThatCode(() -> this.useCase.edit("antiguoNombre", "nuevoNombre", "jaime"))
                .isInstanceOf(AlreadyExists.class);
    }

    @Test
    public void empresaExists(){
        when(this.empresasService.getByNombre("fail")).thenThrow(ResourceNotFound.class);

        assertThatCode(() -> this.useCase.edit("fail", "jahs", "jaime"))
                .isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void correctFormatName(){
        assertThatCode(() -> this.useCase.edit("empresa", null, "jaime")).isInstanceOf(IllegalLength.class);
        assertThatCode(() -> this.useCase.edit("empresa", "", "jaime")).isInstanceOf(IllegalLength.class);
        assertThatCode(() -> this.useCase.edit("empresa", "a".repeat(EmpresasService.MAX_NOMBRE_LONGITUD + 1), "jaime"))
                .isInstanceOf(IllegalLength.class);
    }
}
