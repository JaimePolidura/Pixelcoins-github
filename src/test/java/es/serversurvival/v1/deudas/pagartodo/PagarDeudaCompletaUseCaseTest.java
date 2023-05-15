package es.serversurvival.v1.deudas.pagartodo;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.v1._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.v1.deudas._shared.application.DeudasService;
import es.serversurvival.v1.deudas._shared.domain.Deuda;
import es.serversurvival.v1.deudas.pagarTodo.DeudaPagadaCompletaEvento;
import es.serversurvival.v1.deudas.pagarTodo.PagarDeudaCompletaUseCase;
import es.serversurvival.v1.jugadores.JugadoresTestMother;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static es.serversurvival.v1.MockitoArgEqualsMatcher.of;
import static es.serversurvival.v1.deudas.DeudasTestMother.createDeuda;
import static es.serversurvival.v1.jugadores.JugadoresTestMother.createJugador;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class PagarDeudaCompletaUseCaseTest {
    @Mock private DeudasService deudasService;
    @Mock private JugadoresService jugadoresService;
    @Mock private EventBus eventBus;
    private PagarDeudaCompletaUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new PagarDeudaCompletaUseCase(
                this.deudasService,
                this.jugadoresService,
                this.eventBus
        );
    }

    @Test
    public void shouldPagar(){
        Jugador jugadorDeudor = JugadoresTestMother.createJugador("jaime", 10000);
        Jugador jugadorAcredor = JugadoresTestMother.createJugador("acredor", 10000);
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(jugadorDeudor);
        when(this.jugadoresService.getByNombre("acredor")).thenReturn(jugadorAcredor);
        Deuda deuda = createDeuda("acredor", "jaime");
        when(this.deudasService.getById(deuda.getDeudaId())).thenReturn(deuda);

        this.useCase.pagarDeuda(deuda.getDeudaId(), "jaime");

        verify(this.jugadoresService, times(1)).realizarTransferencia(
                argThat(of(jugadorDeudor)), argThat(of(jugadorAcredor)), anyDouble()
        );
        verify(this.deudasService, times(1)).deleteById(deuda.getDeudaId());
        verify(this.eventBus, times(1)).publish(argThat(of(
                DeudaPagadaCompletaEvento.of(jugadorAcredor.getNombre(), jugadorDeudor.getNombre(), deuda.getPixelcoinsRestantes())
        )));
    }

    @Test
    public void enoughPixelcoins(){
        Jugador jugador = JugadoresTestMother.createJugador("jaime", 0);
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(jugador);
        Deuda deuda = createDeuda("acredor", "jaime");
        when(this.deudasService.getById(deuda.getDeudaId())).thenReturn(deuda);

        assertThatCode(() -> this.useCase.pagarDeuda(deuda.getDeudaId(), "jaime"))
                .isInstanceOf(NotEnoughPixelcoins.class);
    }

    @Test
    public void isDeudor(){
        Deuda deuda = createDeuda("jaime", "otro");
        when(this.deudasService.getById(deuda.getDeudaId())).thenReturn(deuda);
        assertThatCode(() -> this.useCase.pagarDeuda(deuda.getDeudaId(), "jaime"))
                .isInstanceOf(NotTheOwner.class);
    }

    @Test
    public void deudaExists(){
        UUID deudaId = UUID.randomUUID();
        when(this.deudasService.getById(deudaId)).thenThrow(ResourceNotFound.class);
        assertThatCode(() -> this.useCase.pagarDeuda(deudaId, null)).isInstanceOf(ResourceNotFound.class);
    }
}
