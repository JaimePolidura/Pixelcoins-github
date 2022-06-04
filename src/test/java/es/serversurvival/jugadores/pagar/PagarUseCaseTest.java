package es.serversurvival.jugadores.pagar;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.JugadoresRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static es.serversurvival.jugadores.JugadoresTestMother.createJugador;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class PagarUseCaseTest {
    @Mock private JugadoresRepository jugadoresRepository;
    @Mock private EventBus eventBus;
    private PagarUseCase pagarUseCase;
    private JugadoresService jugadoresService;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.jugadoresService = new JugadoresService(this.jugadoresRepository);
        this.pagarUseCase = new PagarUseCase(this.jugadoresService, this.eventBus);
    }

    @Test
    public void makePayment(){
        when(jugadoresRepository.findByNombre("jaime")).thenReturn(Optional.of(createJugador("jaime", 5)));
        when(jugadoresRepository.findByNombre("pedro")).thenReturn(Optional.of(createJugador("pedro", 1)));

        this.pagarUseCase.realizarPago("jaime", "pedro", 2);

        assertThat(this.jugadoresService.getByNombre("jaime"))
                .matches(j -> j.getPixelcoins() == 3)
                .matches(j -> j.getGastos() == 2);
        assertThat(this.jugadoresService.getByNombre("pedro"))
                .matches(j -> j.getPixelcoins() == 3)
                .matches(j -> j.getNventas() == 1)
                .matches(j -> j.getIngresos() == 2);
    }

    @Test
    public void enoughPixelcoins(){
        when(jugadoresRepository.findByNombre("jaime")).thenReturn(Optional.of(createJugador("jaime", 5)));
        when(jugadoresRepository.findByNombre("pedro")).thenReturn(Optional.of(createJugador("pedro", 1)));

        assertThatCode(() -> this.pagarUseCase.realizarPago("jaime", "pedro", 10))
                .isInstanceOf(NotEnoughPixelcoins.class);
    }

    @Test
    public void shouldFindPlayer(){
        when(jugadoresRepository.findByNombre("jaime")).thenReturn(Optional.of(createJugador("jaime")));
        when(jugadoresRepository.findByNombre("pedro")).thenReturn(Optional.of(createJugador("pedro")));
        when(jugadoresRepository.findByNombre("1")).thenThrow(ResourceNotFound.class);
        when(jugadoresRepository.findByNombre("2")).thenThrow(ResourceNotFound.class);

        assertThatCode(() -> this.pagarUseCase.realizarPago("1", "2", 2))
                .isInstanceOf(ResourceNotFound.class);
        assertThatCode(() -> this.pagarUseCase.realizarPago("jaime", "2", 2))
                .isInstanceOf(ResourceNotFound.class);
        assertThatCode(() -> this.pagarUseCase.realizarPago("1", "jaime", 2))
                .isInstanceOf(ResourceNotFound.class);
        assertThatCode(() -> this.pagarUseCase.realizarPago("jaime", "pedro", 2))
                .isNotInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void notSamePlayer(){
        assertThatCode(() -> this.pagarUseCase.realizarPago("jaime", "jaime", 1))
                .isInstanceOf(CannotBeYourself.class);

        assertThatCode(() -> this.pagarUseCase.realizarPago("jaime", "12", 1))
                .isNotInstanceOf(CannotBeYourself.class);
    }

    @Test
    public void correctFormatPixelcoins(){
        assertThatCode(() -> this.pagarUseCase.realizarPago("jaime", "pedro", -1))
                .isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.pagarUseCase.realizarPago("jaime", "pedro", 0))
                .isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.pagarUseCase.realizarPago("jaime", "pedro", 11))
                .isNotInstanceOf(IllegalQuantity.class);
    }
}
