package es.serversurvival.empresas.empresas.editardescripccion;

import es.jaime.javaddd.domain.exceptions.IllegalLength;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.MockitoArgEqualsMatcher;
import es.serversurvival.empresas.empresas.EmpresasTestMother;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static es.serversurvival.MockitoArgEqualsMatcher.of;
import static es.serversurvival.empresas.empresas.EmpresasTestMother.createEmpresa;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class EditarDescripccionUseCaseTest {
    @Mock private EmpresasService empresasService;
    private EditarDescUseCase editarDescUseCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.editarDescUseCase = new EditarDescUseCase(this.empresasService);
    }

    @Test
    public void shouldEditDesc(){
        Empresa empresaToEdit = createEmpresa("empresa", "jaime");
        when(this.empresasService.getByNombre("empresa")).thenReturn(empresaToEdit);

        this.editarDescUseCase.edit("empresa", "gola", "jaime");

        verify(this.empresasService).save(argThat(of(empresaToEdit.withDescripccion("gola"))));
    }

    @Test
    public void ownerOfEmpresa(){
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "otro"));

        assertThatCode(() -> this.editarDescUseCase.edit("empresa", "gola", "jaime"))
                .isInstanceOf(NotTheOwner.class);
    }

    @Test
    public void empresaExists(){
        when(this.empresasService.getByNombre("fail")).thenThrow(ResourceNotFound.class);

        assertThatCode(() -> this.editarDescUseCase.edit("fail", "gola", "jaime"))
                .isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void correctFormatDescripccion(){
        assertThatCode(() -> this.editarDescUseCase.edit("", null, "")).isInstanceOf(IllegalLength.class);
        assertThatCode(() -> this.editarDescUseCase.edit("", "", "")).isInstanceOf(IllegalLength.class);
        assertThatCode(() -> this.editarDescUseCase.edit("", "a".repeat(EmpresasService.MAX_DESC_LONGITUD + 1), ""))
                .isInstanceOf(IllegalLength.class);
    }
}
