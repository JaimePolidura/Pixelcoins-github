package es.serversurvival.jugadores._shared;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival.jugadores._shared.domain.JugadoresRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static es.serversurvival.jugadores.JugadoresTestMother.createJugador;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JugadoresServiceTest {
    @Mock
    private JugadoresRepository jugadoresRepository;
    private JugadoresService jugadoresService;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.jugadoresService = new JugadoresService(this.jugadoresRepository);
    }

    @Test
    public void realizarTransferenciaConEstadisticas(){
        Jugador pagador = createJugador("jaime", 10);
        Jugador pagado = createJugador("pedro", 5);

        this.jugadoresService.realizarTransferencia(pagador, pagado, 5);

        assertThat(this.jugadoresService.getByNombre("jaime"))
                .matches(jugaodr -> jugaodr.getPixelcoins() == 5);
        assertThat(this.jugadoresService.getByNombre("pedro"))
                .matches(jugaodr -> jugaodr.getPixelcoins() == 10);
    }

    @Test
    public void getMapAllJugadores(){
        when(jugadoresRepository.findAll()).thenReturn(List.of(
                createJugador("jaime"),
                createJugador("pedro")
        ));

        assertThat(this.jugadoresService.getMapAllJugadores()).hasSize(2).matches(map -> {
            return map.get("jaime") != null && map.get("pedro") != null;
        });
    }

    @Test
    public void findAll(){
        when(jugadoresRepository.findAll()).thenReturn(List.of(
                createJugador("jaime"),
                createJugador("pedro")
        ));

        assertThat(this.jugadoresService.findAll())
                .hasSize(2);
        assertThat(this.jugadoresService.findBy(j -> j.getNombre().equalsIgnoreCase("jaime")))
                .hasSize(1);
    }

    @Test
    public void save(){
        when(jugadoresRepository.findByNombre("jaime")).thenReturn(Optional.of(createJugador("jaime")));
        when(jugadoresRepository.findByNombre("pedro")).thenReturn(Optional.of(createJugador("pedro")));

        assertThat(this.jugadoresService.getByNombre("jaime"))
                .isNotNull()
                .matches(jugador -> jugador.getNombre().equalsIgnoreCase("jaime"));
        assertThat(this.jugadoresService.getByNombre("pedro"))
                .isNotNull()
                .matches(jugador -> jugador.getNombre().equalsIgnoreCase("pedro"));
        assertThat(this.jugadoresService.getByNombre("jaime"))
                .isNotNull()
                .matches(jugador -> jugador.getNombre().equalsIgnoreCase("jaime"));

        verify(jugadoresRepository, times(1)).findByNombre("jaime");
        verify(jugadoresRepository, times(1)).findByNombre("pedro");

        assertThatCode(() -> this.jugadoresService.getByNombre("tuputamadre"))
                .isInstanceOf(ResourceNotFound.class);
    }
}
