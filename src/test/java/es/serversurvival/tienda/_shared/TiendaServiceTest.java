package es.serversurvival.tienda._shared;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.tienda._shared.application.TiendaService;
import es.serversurvival.tienda._shared.domain.TiendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static es.serversurvival.tienda.TiendaTestMother.createTiendaObjeto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class TiendaServiceTest {
    @Mock
    private TiendaRepository tiendaRepository;
    private TiendaService tiendaService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        this.tiendaService = new TiendaService(this.tiendaRepository);
    }

    @Test
    public void saveWithoutErrors(){
        this.tiendaService.save(createTiendaObjeto("jaime"));
    }

    @Test
    public void getById(){
        var id= UUID.randomUUID();
        when(this.tiendaRepository.findById(id)).thenReturn(Optional.of(createTiendaObjeto("jaime")));
        var id2= UUID.randomUUID();
        when(this.tiendaRepository.findById(id2)).thenReturn(Optional.of(createTiendaObjeto("jaime")));

        assertThat(this.tiendaService.getById(id)).isNotNull().matches(objeto -> objeto.getJugador().equalsIgnoreCase("jaime"));
        assertThat(this.tiendaService.getById(id2)).isNotNull().matches(objeto -> objeto.getJugador().equalsIgnoreCase("jaime"));
        assertThat(this.tiendaService.getById(id)).isNotNull().matches(objeto -> objeto.getJugador().equalsIgnoreCase("jaime"));
        assertThatCode(() -> this.tiendaService.getById(UUID.randomUUID())).isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void findByJugador(){
        when(this.tiendaRepository.findAll()).thenReturn(List.of(
                createTiendaObjeto("jaime"),
                createTiendaObjeto("jaime", 2)
        ));
        this.tiendaService.findAll();

        assertThat(this.tiendaService.findByJugador("jaime")).hasSize(2).allMatch(objeto -> objeto.getJugador().equalsIgnoreCase("jaime"));
        assertThat(this.tiendaService.findByJugador("nadie")).isEmpty();
        assertThat(this.tiendaService.findByJugador("jaime")).hasSize(2).allMatch(objeto -> objeto.getJugador().equalsIgnoreCase("jaime"));
    }

    @Test
    public void findAll(){
        when(this.tiendaRepository.findAll()).thenReturn(List.of(
                createTiendaObjeto("jaime"), createTiendaObjeto("jaime")
        ));

        assertThat(this.tiendaService.findAll()).hasSize(2).allMatch(t -> t.getJugador().equalsIgnoreCase("jaime"));
        assertThat(this.tiendaService.findAll()).hasSize(2).allMatch(t -> t.getJugador().equalsIgnoreCase("jaime"));
    }

    @Test
    public void deleteById(){
        when(this.tiendaRepository.findAll()).thenReturn(List.of(
                createTiendaObjeto("jaime"),
                createTiendaObjeto("jaime", 2)
        ));
        var idToRemove = this.tiendaService.findAll().get(0).getTiendaObjetoId();
        this.tiendaService.deleteById(idToRemove);

        assertThat(this.tiendaService.findAll()).hasSize(1).allMatch(t -> !t.getTiendaObjetoId().equals(idToRemove));
    }
}
