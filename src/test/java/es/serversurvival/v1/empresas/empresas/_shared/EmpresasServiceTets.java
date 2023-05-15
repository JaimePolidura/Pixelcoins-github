package es.serversurvival.v1.empresas.empresas._shared;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.v1.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.v1.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.v1.empresas.empresas._shared.domain.EmpresasRepostiory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static es.serversurvival.v1.empresas.empresas.EmpresasTestMother.createEmpresa;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class EmpresasServiceTets {
    @Mock
    private EmpresasRepostiory empresasRepostiory;
    private EmpresasService empresasService;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.empresasService = new EmpresasService(this.empresasRepostiory);
    }

    @Test
    public void deleteByEmpresaId(){
        UUID empresaToRemove = UUID.randomUUID();
        Empresa empresa = createEmpresa("jaime", "jaime");
        this.empresasService.save(empresa);

        this.empresasService.deleteByEmpresaId(empresaToRemove);

        assertThatCode(() -> this.empresasService.getById(empresaToRemove)).isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void getAllPixelcoinsEnEmpresas(){
        when(this.empresasRepostiory.findByOwner("jaime")).thenReturn(List.of(
                createEmpresa("jaime", "jaime", 10),
                createEmpresa("jaime2", "jaime", 10)
        ));

        assertThat(this.empresasService.getAllPixelcoinsEnEmpresas("jaime")).isEqualTo(20);
    }

    @Test
    public void getAll(){
        when(this.empresasRepostiory.findAll()).thenReturn(List.of(
                createEmpresa("jaime", "jaime"), createEmpresa("jaime2", "jaime")
        ));

        Assertions.assertThat(this.empresasService.findAll()).hasSize(2);
    }

    @Test
    public void getByOwner(){
        when(this.empresasRepostiory.findByOwner("jaime")).thenReturn(List.of(
                createEmpresa("jaime", "jaime"), createEmpresa("jaime2", "jaime")
        ));

        Assertions.assertThat(this.empresasService.getByOwner("jaime")).hasSize(2);
        Assertions.assertThat(this.empresasService.getByOwner("jaime")).hasSize(2);
    }

    @Test
    public void getByNombre(){
        Empresa empresaToSave = createEmpresa("empresa", "jaime");
        this.empresasService.save(empresaToSave);

        Assertions.assertThat(this.empresasService.getByNombre(empresaToSave.getNombre())).isNotNull()
                .matches(empresa -> empresa.getNombre().equalsIgnoreCase(empresaToSave.getNombre()));
        Assertions.assertThat(this.empresasService.getById(empresaToSave.getEmpresaId())).isNotNull()
                .matches(empresa -> empresa.getNombre().equalsIgnoreCase(empresaToSave.getNombre()));

        String nombreEmpresToFail = "empreson";
        when(this.empresasRepostiory.findByNombre(nombreEmpresToFail)).thenReturn(Optional.empty());

        assertThatCode(() -> this.empresasService.getByNombre(nombreEmpresToFail)).isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void getById(){
        Empresa empresaToSave = createEmpresa("empresa", "jaime");
        this.empresasService.save(empresaToSave);

        Assertions.assertThat(this.empresasService.getById(empresaToSave.getEmpresaId())).isNotNull()
                .matches(empresa -> empresa.getNombre().equalsIgnoreCase(empresaToSave.getNombre()));
        Assertions.assertThat(this.empresasService.getById(empresaToSave.getEmpresaId())).isNotNull()
                .matches(empresa -> empresa.getNombre().equalsIgnoreCase(empresaToSave.getNombre()));

        UUID idEmpresaToFail = UUID.randomUUID();
        when(this.empresasRepostiory.findById(idEmpresaToFail)).thenReturn(Optional.empty());

        assertThatCode(() -> this.empresasService.getById(idEmpresaToFail)).isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void save(){
        Empresa empresaToSave = createEmpresa("empresa", "jaime");
        this.empresasService.save(empresaToSave);
        verify(this.empresasRepostiory, times(1)).save(empresaToSave);
    }


}
