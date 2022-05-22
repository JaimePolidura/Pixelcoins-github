package es.serversurvival.deudas.prestar;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.deudas._shared.application.DeudasService;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static es.serversurvival.jugadores.JugadoresTestMother.createJugador;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

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
    public void acredorHasEnoughPixelcoins(){
        when(this.jugadoresService.getByNombre("acredor")).thenReturn(createJugador("acredor", 10));
        assertThatCode(() -> this.useCase.prestar("acredor", "otro", 20, 1, 2))
                .isInstanceOf(NotEnoughPixelcoins.class);

        assertThatCode(() -> this.useCase.prestar("jaime", "otro", 10, 1, 1))
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
