package es.serversurvival.bolsa.posicionesabiertas.comprarlargo;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbiertaEvento;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static es.serversurvival.MockitoArgEqualsMatcher.of;
import static es.serversurvival.bolsa.activosinfo.ActivosInfoTestMother.createActivoInfoAcciones;
import static es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo.*;
import static es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion.LARGO;
import static es.serversurvival.jugadores.JugadoresTestMother.createJugador;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ComprarLargoUseCaseTest {
    @Mock private PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    @Mock private JugadoresService jugadoresService;
    @Mock private ActivosInfoService activoInfoService;
    @Mock private EventBus eventBus;
    private ComprarLargoUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new ComprarLargoUseCase(
                this.posicionesAbiertasSerivce,
                this.jugadoresService,
                this.activoInfoService,
                this.eventBus
        );
    }

    @Test
    public void shouldComprarLargo(){
        when(this.activoInfoService.getByNombreActivo("AMZN", ACCIONES)).thenReturn(
                createActivoInfoAcciones("AMZN").withPrecio(100).withNombreActivoLargo("Amazon")
        );
        Jugador jugadorComprador = createJugador("jaime", 10000);
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(jugadorComprador);

        this.useCase.comprarLargo("jaime", ACCIONES, "AMZN", 2);

        ArgumentCaptor<Integer> cantidadArgCaptor = ArgumentCaptor.forClass(int.class);
        ArgumentCaptor<Double> precioUnidadArgCaptor = ArgumentCaptor.forClass(double.class);
        verify(this.posicionesAbiertasSerivce, times(1)).save(
                argThat(of("jaime")), argThat(of(ACCIONES)), argThat(of("AMZN")),cantidadArgCaptor.capture(),
                precioUnidadArgCaptor.capture(), argThat(of(LARGO))
        );
        assertThat(cantidadArgCaptor.getValue()).isEqualTo(2);
        assertThat(precioUnidadArgCaptor.getValue()).isEqualTo(100);
    
        verify(this.jugadoresService, times(1)).save(argThat(of(
                jugadorComprador.decrementPixelcoinsBy(100 * 2).incrementGastosBy(100 * 2)
        )));

        verify(this.eventBus, times(1)).publish(argThat(of(
                PosicionAbiertaEvento.of("jaime", "AMZN", 2, 100, ACCIONES, 100 * 2, "Amazon", LARGO)
        )));
    }

    @Test
    public void enoughPixelcoins(){
        when(this.activoInfoService.getByNombreActivo("AMZN", ACCIONES)).thenReturn(
                createActivoInfoAcciones("AMZN").withPrecio(1000)
        );
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(
                createJugador("jaime", 10)
        );

        assertThatCode(() -> this.useCase.comprarLargo("jaime", ACCIONES, "AMZN", 2))
                .isInstanceOf(NotEnoughPixelcoins.class);
    }

    @Test
    public void activoExists(){
        when(this.activoInfoService.getByNombreActivo("AMZN", MATERIAS_PRIMAS)).thenReturn(null);

        assertThatCode(() -> this.useCase.comprarLargo("jaime", MATERIAS_PRIMAS, "AMZN", 1))
                .isInstanceOf(ResourceNotFound.class);
    }
}
