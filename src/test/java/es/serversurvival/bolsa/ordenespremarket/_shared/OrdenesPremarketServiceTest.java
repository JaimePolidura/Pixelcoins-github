package es.serversurvival.bolsa.ordenespremarket._shared;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrdenesPremarketService;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.OrderesPremarketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static es.serversurvival.bolsa.ordenespremarket.OrdernesPremarketTestMother.createOrdenPremarket;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class OrdenesPremarketServiceTest {
    @Mock private OrderesPremarketRepository repositoryDb;
    private OrdenesPremarketService service;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.service = new OrdenesPremarketService(this.repositoryDb);
    }

    @Test
    public void getById(){
        UUID idToGet = UUID.randomUUID();
        when(this.repositoryDb.findById(idToGet)).thenReturn(Optional.empty());
        assertThatCode(() -> this.service.getById(idToGet))
                .isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void deleteById(){
        UUID idToDelete = UUID.randomUUID();
        this.service.deleteById(idToDelete);
        verify(this.repositoryDb, times(1)).deleteById(idToDelete);
    }

    @Test
    public void findByJugador(){
        when(this.repositoryDb.findByJugador("jaime")).thenReturn(List.of(
                createOrdenPremarket("jaime", "AMZN"),
                createOrdenPremarket("jaime", "META")
        ));
        assertThat(this.service.findByJugador("jaime"))
                .hasSize(2)
                .allMatch(p -> p.getJugador().equalsIgnoreCase("jaime"));
    }

    @Test
    public void findAll(){
        when(this.repositoryDb.findAll()).thenReturn(List.of(
                createOrdenPremarket("jaime", "AMZN"),
                createOrdenPremarket("jaime", "META")
        ));
        assertThat(this.service.findAll())
                .hasSize(2)
                .allMatch(p -> p.getJugador().equalsIgnoreCase("jaime"));
    }

    @Test
    public void isOrdenRegisteredFromPosicionAbierta(){
        UUID posicionAbiertoToSearch = UUID.randomUUID();
        when(this.repositoryDb.findByJugador("jaime")).thenReturn(List.of(
                createOrdenPremarket("jaime", "AMZN", posicionAbiertoToSearch),
                createOrdenPremarket("jaime", "META", UUID.randomUUID())
        ));
        assertThat(this.service.isOrdenRegisteredFromPosicionAbierta("jaime", posicionAbiertoToSearch))
                .isTrue();
        assertThat(this.service.isOrdenRegisteredFromPosicionAbierta("jaime", UUID.randomUUID()))
                .isFalse();
    }

    @Test
    public void save(){
        var ordenToSave = createOrdenPremarket("jaime", "AMZN");
        this.service.save(ordenToSave);
        verify(this.repositoryDb, times(1)).save(ordenToSave);
    }
}
