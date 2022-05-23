package es.serversurvival.bolsa.ordenespremarket.ejecutarordenes;

import es.jaime.EventBus;
import es.serversurvival.bolsa.activosinfo.ActivosInfoTestMother;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrdenesPremarketService;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.OrdenPremarket;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.bolsa.posicionesabiertas.comprarcorto.ComprarCortoUseCase;
import es.serversurvival.bolsa.posicionesabiertas.comprarlargo.ComprarLargoUseCase;
import es.serversurvival.bolsa.posicionesabiertas.vendercorto.VenderCortoUseCase;
import es.serversurvival.bolsa.posicionesabiertas.venderlargo.VenderLargoUseCase;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static es.serversurvival.MockitoArgEqualsMatcher.*;
import static es.serversurvival.bolsa.posicionesabiertas.PosicionesAbiertasTestMother.createPosicionAbierta;
import static es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo.ACCIONES;
import static es.serversurvival.bolsa.ordenespremarket.OrdernesPremarketTestMother.createOrdenPremarket;
import static es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion.*;
import static es.serversurvival.jugadores.JugadoresTestMother.createJugador;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class EjecutarOrdenesPreMarketUseCaseTest {
    @Mock private JugadoresService jugadoresService;
    @Mock private OrdenesPremarketService ordenesPremarketService;
    @Mock private PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    @Mock private ActivosInfoService activoInfoService;
    @Mock private EventBus eventBus;
    @Mock private ComprarLargoUseCase comprarLargoUseCase;
    @Mock private VenderLargoUseCase venderLargoUseCase;
    @Mock private VenderCortoUseCase venderCortoUseCase;
    @Mock private ComprarCortoUseCase comprarCortoUseCase;
    private EjecutarOrdenesPreMarketUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = createUseCase();
    }

    @Test
    public void compraCortoNotExecuted(){
        PosicionAbierta posicionAbierta = createPosicionAbierta("jaime", "AMZN").withCantidad(4);
        OrdenPremarket ordenVentaLargo = createOrdenPremarket("jaime", "AMZN", posicionAbierta.getPosicionAbiertaId(),
                CORTO_COMPRA, 200);
        when(this.ordenesPremarketService.findAll()).thenReturn(List.of(ordenVentaLargo));
        when(this.posicionesAbiertasSerivce.getById(posicionAbierta.getPosicionAbiertaId())).thenReturn(posicionAbierta);

        this.useCase.ejecutarOrdenes();

        verify(this.eventBus, times(1)).publish(argThat(of(
                OrdenNoEjecutadoEvento.of("jaime", "AMZN", ordenVentaLargo.getCantidad())
        )));
        verify(this.ordenesPremarketService, times(1)).deleteById(ordenVentaLargo.getOrderPremarketId());
    }

    @Test
    public void compraCortoExecute(){
        PosicionAbierta posicionAbierta = createPosicionAbierta("jaime", "AMZN").withCantidad(4);
        OrdenPremarket ordenVentaLargo = createOrdenPremarket("jaime", "AMZN", posicionAbierta.getPosicionAbiertaId(), CORTO_COMPRA, 2);
        when(this.ordenesPremarketService.findAll()).thenReturn(List.of(ordenVentaLargo));
        when(this.posicionesAbiertasSerivce.getById(posicionAbierta.getPosicionAbiertaId())).thenReturn(posicionAbierta);

        this.useCase.ejecutarOrdenes();

        ArgumentCaptor<Integer> quantityArgumentCapturer = ArgumentCaptor.forClass(Integer.class);
        verify(this.comprarCortoUseCase, times(1)).comprarPosicionCorto(
                argThat(of(posicionAbierta.getPosicionAbiertaId())),
                quantityArgumentCapturer.capture(),
                argThat(of("jaime"))
        );
        assertThat(quantityArgumentCapturer.getValue()).isEqualTo(ordenVentaLargo.getCantidad());
        verify(this.ordenesPremarketService, times(1)).deleteById(ordenVentaLargo.getOrderPremarketId());
    }

    @Test
    public void ventaCortoNotExecuted(){
        OrdenPremarket ordenCompraLargo = createOrdenPremarket("jaime", "AMZN", null, CORTO_VENTA, 2);
        Jugador jugador = createJugador("jaime", 0);
        ActivoInfo activoInfo = ActivosInfoTestMother.createActivoInfoAcciones("AMZN").withPrecio(20).withNombreActivoLargo("Amazon");
        when(this.ordenesPremarketService.findAll()).thenReturn(List.of(ordenCompraLargo));
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(jugador);
        when(this.activoInfoService.getByNombreActivo("AMZN", ACCIONES)).thenReturn(activoInfo);

        this.useCase.ejecutarOrdenes();

        verify(this.eventBus, times(1)).publish(argThat(of(
                OrdenNoEjecutadoEvento.of("jaime", "AMZN", 2)
        )));
        verify(this.ordenesPremarketService, times(1)).deleteById(ordenCompraLargo.getOrderPremarketId());
    }

    @Test
    public void ventaCortoExecute(){
        OrdenPremarket ordenCompraLargo = createOrdenPremarket("jaime", "AMZN", null, CORTO_VENTA, 2);
        Jugador jugador = createJugador("jaime", 100);
        ActivoInfo activoInfo = ActivosInfoTestMother.createActivoInfoAcciones("AMZN").withPrecio(20).withNombreActivoLargo("Amazon");
        when(this.ordenesPremarketService.findAll()).thenReturn(List.of(ordenCompraLargo));
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(jugador);
        when(this.activoInfoService.getByNombreActivo("AMZN", ACCIONES)).thenReturn(activoInfo);

        this.useCase.ejecutarOrdenes();

        ArgumentCaptor<Integer> cantidadArgCapturer = ArgumentCaptor.forClass(int.class);
        ArgumentCaptor<Double> precioArgCapturer = ArgumentCaptor.forClass(double.class);
        verify(this.venderCortoUseCase, times(1)).venderEnCortoBolsa(
                argThat(of("jaime")),
                argThat(of("AMZN")),
                argThat(of("Amazon")),
                cantidadArgCapturer.capture(),
                precioArgCapturer.capture()
        );
        assertThat(cantidadArgCapturer.getValue()).isEqualTo(ordenCompraLargo.getCantidad());
        assertThat(precioArgCapturer.getValue()).isEqualTo(activoInfo.getPrecio());
        verify(this.ordenesPremarketService, times(1)).deleteById(ordenCompraLargo.getOrderPremarketId());
    }

    @Test
    public void ventaLargoNotExecuted(){
        PosicionAbierta posicionAbierta = createPosicionAbierta("jaime", "AMZN").withCantidad(1);
        OrdenPremarket ordenCompraLargo = createOrdenPremarket("jaime", "AMZN", posicionAbierta.getPosicionAbiertaId(), LARGO_VENTA, 4);
        when(this.ordenesPremarketService.findAll()).thenReturn(List.of(ordenCompraLargo));
        when(this.posicionesAbiertasSerivce.getById(posicionAbierta.getPosicionAbiertaId())).thenReturn(posicionAbierta);

        this.useCase.ejecutarOrdenes();

        verify(this.eventBus, times(1)).publish(argThat(of(
                OrdenNoEjecutadoEvento.of("jaime", "AMZN", 4)
        )));
        verify(this.ordenesPremarketService, times(1)).deleteById(ordenCompraLargo.getOrderPremarketId());
    }

    @Test
    public void ventaLargoExecute(){
        PosicionAbierta posicionAbierta = createPosicionAbierta("jaime", "AMZN").withCantidad(4);
        OrdenPremarket ordenVentaLargo = createOrdenPremarket("jaime", "AMZN", posicionAbierta.getPosicionAbiertaId(), LARGO_VENTA, 2);
        when(this.ordenesPremarketService.findAll()).thenReturn(List.of(ordenVentaLargo));
        when(this.posicionesAbiertasSerivce.getById(posicionAbierta.getPosicionAbiertaId())).thenReturn(posicionAbierta);

        this.useCase.ejecutarOrdenes();

        ArgumentCaptor<Integer> quantityArgumentCapturer = ArgumentCaptor.forClass(Integer.class);
        verify(this.venderLargoUseCase, times(1)).venderPosicion(
                argThat(of(posicionAbierta)),
                quantityArgumentCapturer.capture(),
                argThat(of("jaime"))
        );
        assertThat(quantityArgumentCapturer.getValue()).isEqualTo(ordenVentaLargo.getCantidad());
        verify(this.ordenesPremarketService, times(1)).deleteById(ordenVentaLargo.getOrderPremarketId());
    }

    @Test
    public void compraLargoNotExecuted(){
        OrdenPremarket ordenCompraLargo = createOrdenPremarket("jaime", "AMZN", null, LARGO_COMPRA, 2);
        Jugador jugador = createJugador("jaime", 0);
        ActivoInfo activoInfo = ActivosInfoTestMother.createActivoInfoAcciones("AMZN").withPrecio(20);
        when(this.ordenesPremarketService.findAll()).thenReturn(List.of(ordenCompraLargo));
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(jugador);
        when(this.activoInfoService.getByNombreActivo("AMZN", ACCIONES)).thenReturn(activoInfo);

        this.useCase.ejecutarOrdenes();

        verify(this.eventBus, times(1)).publish(argThat(of(
                OrdenNoEjecutadoEvento.of("jaime", "AMZN", 2)
        )));
        verify(this.ordenesPremarketService, times(1)).deleteById(ordenCompraLargo.getOrderPremarketId());
    }

    @Test
    public void compraLargoExecute(){
        OrdenPremarket ordenCompraLargo = createOrdenPremarket("jaime", "AMZN", null, LARGO_COMPRA, 2);
        Jugador jugador = createJugador("jaime", 100);
        ActivoInfo activoInfo = ActivosInfoTestMother.createActivoInfoAcciones("AMZN").withPrecio(20);
        when(this.ordenesPremarketService.findAll()).thenReturn(List.of(ordenCompraLargo));
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(jugador);
        when(this.activoInfoService.getByNombreActivo("AMZN", ACCIONES)).thenReturn(activoInfo);

        this.useCase.ejecutarOrdenes();

        ArgumentCaptor<Integer> cantidadArgCapturer = ArgumentCaptor.forClass(int.class);
        verify(this.comprarLargoUseCase, times(1)).comprarLargo(
                argThat(of(ACCIONES)),
                argThat(of("AMZN")),
                cantidadArgCapturer.capture(),
                argThat(of("jaime"))
        );
        assertThat(cantidadArgCapturer.getValue()).isEqualTo(ordenCompraLargo.getCantidad());
        verify(this.ordenesPremarketService, times(1)).deleteById(ordenCompraLargo.getOrderPremarketId());
    }

    private EjecutarOrdenesPreMarketUseCase createUseCase() {
        return new EjecutarOrdenesPreMarketUseCase(new AtomicBoolean(false),
                this.jugadoresService, this.ordenesPremarketService, this.posicionesAbiertasSerivce, this.activoInfoService, this.eventBus,
                this.comprarLargoUseCase, this.venderLargoUseCase, this.venderCortoUseCase, this.comprarCortoUseCase
        );
    }
}
