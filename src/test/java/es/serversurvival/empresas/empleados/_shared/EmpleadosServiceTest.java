package es.serversurvival.empresas.empleados._shared;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.cache.LRUCache;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empleados._shared.domain.EmpleadosRepository;
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

import static es.serversurvival.empresas.empleados.EmpleadosTestMother.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class EmpleadosServiceTest {
    @Mock private EmpleadosRepository repositoryDb;
    private EmpleadosService service;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.service = new EmpleadosService(this.repositoryDb, new LRUCache<>(150));
    }

    @Test
    public void deleteById(){
        this.service.deleteById(UUID.randomUUID());

        verify(this.repositoryDb, times(1)).deleteById(any(UUID.class));
    }

    @Test
    public void findAll(){
        Empleado empleado1 = createEmpleado("jaime", "empresa");
        Empleado empleado2 = createEmpleado("otro", "empresa");
        when(this.repositoryDb.findAll()).thenReturn(List.of(empleado1, empleado2));

        assertThat(this.service.findAll()).hasSize(2);
    }

    @Test
    public void findByEmpresa(){
        Empleado empleado1 = createEmpleado("jaime", "empresa");
        Empleado empleado2 = createEmpleado("otro", "empresa");
        when(this.repositoryDb.findByEmpresa("empresa")).thenReturn(List.of(empleado1, empleado2));

        assertThat(this.service.findByEmpresa("empresa")).hasSize(2).allMatch(e -> e.getEmpresa().equalsIgnoreCase("empresa"));
    }

    @Test
    public void findByJugador(){
        Empleado empleado = createEmpleado("jaime", "empresa");
        when(this.repositoryDb.findByJugador("jaime")).thenReturn(List.of(empleado, createEmpleado("jaime", "empresa2")));

        assertThat(this.service.findByJugador("jaime")).hasSize(2).allMatch(e -> e.getNombre().equalsIgnoreCase("jaime"));
    }

    @Test
    public void getEmpleadoInEmpresa(){
        Empleado empleado = createEmpleado("jaime", "empresa");
        when(this.repositoryDb.findByJugador("jaime")).thenReturn(List.of(empleado, createEmpleado("jaime", "empresa2")));

        assertThat(this.service.getEmpleadoInEmpresa(empleado.getNombre(), "empresa")).isNotNull();
        assertThat(this.service.getEmpleadoInEmpresa(empleado.getNombre(), "empresa")).isNotNull();

        assertThatCode(() -> this.service.getEmpleadoInEmpresa("jaime", "kajsñlxd"))
                .isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void getById(){
        Empleado empleado = createEmpleado("jaime", "empresa");
        this.service.save(empleado);

        assertThat(this.service.getById(empleado.getEmpleadoId())).isNotNull();
        assertThat(this.service.getById(empleado.getEmpleadoId())).isNotNull();

        UUID idToSearch = UUID.randomUUID();
        when(this.repositoryDb.findById(idToSearch)).thenReturn(Optional.empty());
        assertThatCode(() -> this.service.getById(idToSearch)).isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void save(){
        Empleado empleadoToSave = createEmpleado("jaime", "empresa");
        this.service.save(empleadoToSave);
        verify(this.repositoryDb, times(1)).save(empleadoToSave);
    }
}
