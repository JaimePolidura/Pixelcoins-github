package es.serversurvival.v1.empresas.empresas.editarlogotipo;

import es.jaime.javaddd.domain.exceptions.IllegalType;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.v1.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.v1.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.v1.empresas.empresas.logitipo.EditarLogitpoUseCase;
import org.bukkit.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static es.serversurvival.v1.MockitoArgEqualsMatcher.of;
import static es.serversurvival.v1.empresas.empresas.EmpresasTestMother.createEmpresa;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public final class EditarLogotipoUseCaseTest {
    @Mock private EmpresasService empresasService;
    private EditarLogitpoUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new EditarLogitpoUseCase(this.empresasService);
    }

    @Test
    public void shouldEditLogotipo(){
        Empresa empresaToEdit = createEmpresa("empresa", "jaime");
        
        when(this.empresasService.getByNombre("empresa")).thenReturn(empresaToEdit);
        this.useCase.cambiar("empresa", Material.COAL, "jaime");

        verify(this.empresasService, times(1)).save(argThat(of(
                empresaToEdit.withIcono(Material.COAL)
        )));
    }

    @Test
    public void ownerOfEmpresa(){
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "otro"));
        assertThatCode(() -> this.useCase.cambiar("empresa", Material.COAL, "jaime")).isInstanceOf(NotTheOwner.class);
    }

    @Test
    public void empresaExists(){
        when(this.empresasService.getByNombre("empresa")).thenThrow(ResourceNotFound.class);

        assertThatCode(() -> this.useCase.cambiar("empresa", Material.COAL, "")).isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void correctFormatLogitipo(){
        assertThatCode(() -> this.useCase.cambiar("", null, "")).isInstanceOf(IllegalType.class);
        assertThatCode(() -> this.useCase.cambiar("", Material.AIR, "")).isInstanceOf(IllegalType.class);
    }
}
