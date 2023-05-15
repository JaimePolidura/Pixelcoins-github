package es.serversurvival.v1.deudas.prestar;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.serversurvival.v1._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.v1.deudas._shared.application.DeudasService;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import es.serversurvival.v1.jugadores.JugadoresTestMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static es.serversurvival.v1.MockitoArgEqualsMatcher.of;
import static es.serversurvival.v1.jugadores.JugadoresTestMother.createJugador;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class PrestarUseCaseTest {
    @Mock private JugadoresService jugadoresService;
    @Mock private DeudasService deudasService;
    @Mock private EventBus eventBus;
    private PrestarUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new PrestarUseCase(
                this.jugadoresService,
                this.deudasService,
                this.eventBus
        );
    }

    @Test
    public void shoulPrestar(){
        Jugador acredorJugador = JugadoresTestMother.createJugador("acredor", 100);
        Jugador deudorJugador = JugadoresTestMother.createJugador("deudor", 10);
        when(this.jugadoresService.getByNombre("acredor")).thenReturn(acredorJugador);
        when(this.jugadoresService.getByNombre("deudor")).thenReturn(deudorJugador);
        ArgumentCaptor<Double> pixelcoinsConInteresesCapturar = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Integer> interesesCapturar = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> diasCapturar = ArgumentCaptor.forClass(Integer.class);

        this.useCase.prestar("acredor", "deudor", 10, 20, 2);

        verify(this.deudasService, times(1)).save(
                argThat(of("deudor")), argThat(of("acredor")), pixelcoinsConInteresesCapturar.capture(),
                diasCapturar.capture(), interesesCapturar.capture()
        );

        assertThat(pixelcoinsConInteresesCapturar.getValue()).isEqualTo(12);
        assertThat(interesesCapturar.getValue()).isEqualTo(20);
        assertThat(diasCapturar.getValue()).isEqualTo(2);

        verify(this.jugadoresService, times(1)).realizarTransferencia(
                argThat(of(acredorJugador)), argThat(of(deudorJugador)), anyDouble()
        );
        verify(this.eventBus, times(1)).publish(argThat(of(
                PixelcoinsPrestadasEvento.of("acredor", "deudor", 10, 20, 2)
        )));
    }

    @Test
    public void acredorHasEnoughPixelcoins(){
        when(this.jugadoresService.getByNombre("acredor")).thenReturn(JugadoresTestMother.createJugador("acredor", 10));
        assertThatCode(() -> this.useCase.prestar("acredor", "otro", 20, 1, 2))
                .isInstanceOf(NotEnoughPixelcoins.class);

        assertThatCode(() -> this.useCase.prestar("acredor", "otro", 10, 5, 1))
                .isInstanceOf(NotEnoughPixelcoins.class);
    }

    @Test
    public void pixelcoinsAndDiasAndInteresCorrectFomart(){
        assertThatCode(() -> this.useCase.prestar("jaime", "otro", 0, 1, 1))
                .isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.prestar("jaime", "otro", -1, 1, 1))
                .isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.prestar("jaime", "otro", 1, -1, 1))
                .isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.prestar("jaime", "otro", 1, 1, 0))
                .isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.prestar("jaime", "otro", 1, 1, -1))
                .isInstanceOf(IllegalQuantity.class);
    }

    @Test
    public void ensureNotTheSame(){
        assertThatCode(() -> this.useCase.prestar("jaime", "jaime", 1, 1, 1))
                .isInstanceOf(CannotBeYourself.class);
    }
}
