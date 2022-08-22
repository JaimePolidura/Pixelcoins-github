package es.serversurvival.bolsa.posicionesabiertas.vendercorto;

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
import static es.serversurvival._shared.utils.Funciones.redondeoDecimales;
import static es.serversurvival._shared.utils.Funciones.reducirPorcentaje;
import static es.serversurvival.bolsa.activosinfo.ActivosInfoTestMother.createActivoInfoAcciones;
import static es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo.ACCIONES;
import static es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce.PORCENTAJE_CORTO;
import static es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion.CORTO;
import static es.serversurvival.jugadores.JugadoresTestMother.createJugador;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class VenderCortoUseCaseTest {
    @Mock private PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    @Mock private JugadoresService jugadoresService;
    @Mock private ActivosInfoService activosInfoService;
    @Mock private EventBus eventBus;
    private VenderCortoUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new VenderCortoUseCase(
                this.posicionesAbiertasSerivce,
                this.jugadoresService,
                this.activosInfoService,
                this.eventBus
        );
    }

    @Test
    public void shouldComprarLargo(){
        when(this.activosInfoService.getByNombreActivo("AMZN", ACCIONES)).thenReturn(
                createActivoInfoAcciones("AMZN").withPrecio(100).withNombreActivoLargo("Amazon")
        );
        Jugador jugadorComprador = createJugador("jaime", 10000);
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(jugadorComprador);

        this.useCase.venderEnCortoBolsa("jaime","AMZN", 2);

        ArgumentCaptor<Integer> cantidadArgCaptor = ArgumentCaptor.forClass(int.class);
        ArgumentCaptor<Double> precioUnidadArgCaptor = ArgumentCaptor.forClass(double.class);
        verify(this.posicionesAbiertasSerivce, times(1)).save(
                argThat(of("jaime")), argThat(of(ACCIONES)), argThat(of("AMZN")),cantidadArgCaptor.capture(),
                precioUnidadArgCaptor.capture(), argThat(of(CORTO))
        );
        assertThat(cantidadArgCaptor.getValue()).isEqualTo(2);
        assertThat(precioUnidadArgCaptor.getValue()).isEqualTo(100);
        double comisionTotal = redondeoDecimales(reducirPorcentaje(2 * 100, 100 - PORCENTAJE_CORTO), 2);
        verify(this.jugadoresService, times(1)).save(argThat(of(
                jugadorComprador.decrementPixelcoinsBy(comisionTotal).incrementGastosBy(comisionTotal)
        )));

        verify(this.eventBus, times(1)).publish(argThat(of(
                PosicionAbiertaEvento.of("jaime", "AMZN", 2, 100, ACCIONES, comisionTotal, "Amazon", CORTO)
        )));
    }


    @Test
    public void enoughPixelcoins(){
        when(this.activosInfoService.getByNombreActivo("AMZN", ACCIONES)).thenReturn(
                createActivoInfoAcciones("AMZN").withPrecio(1000)
        );
        double comisionTotal = 1000 * 2 * 0.05;
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(
                createJugador("jaime", comisionTotal - 1)
        );

        assertThatCode(() -> this.useCase.venderEnCortoBolsa("jaime", "AMZN", 2))
                .isInstanceOf(NotEnoughPixelcoins.class);
    }

    @Test
    public void activoExists(){
        when(this.activosInfoService.getByNombreActivo("AMZN", ACCIONES)).thenReturn(null);

        assertThatCode(() -> this.useCase.venderEnCortoBolsa("jaime", "AMZN", 1))
                .isInstanceOf(ResourceNotFound.class);
    }
}
