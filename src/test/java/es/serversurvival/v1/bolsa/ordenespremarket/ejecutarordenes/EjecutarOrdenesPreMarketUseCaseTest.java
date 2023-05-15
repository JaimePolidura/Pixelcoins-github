package es.serversurvival.v1.bolsa.ordenespremarket.ejecutarordenes;

import es.jaime.EventBus;
import es.serversurvival.v1.bolsa.activosinfo.ActivosInfoTestMother;
import es.serversurvival.v1.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.v1.bolsa.ordenespremarket._shared.application.OrdenesPremarketService;
import es.serversurvival.v1.bolsa.ordenespremarket._shared.domain.OrdenPremarket;
import es.serversurvival.v1.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.v1.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.v1.bolsa.posicionesabiertas.comprarcorto.ComprarCortoUseCase;
import es.serversurvival.v1.bolsa.posicionesabiertas.comprarlargo.ComprarLargoUseCase;
import es.serversurvival.v1.bolsa.posicionesabiertas.vendercorto.VenderCortoUseCase;
import es.serversurvival.v1.bolsa.posicionesabiertas.venderlargo.VenderLargoUseCase;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.v1.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival.v1.bolsa.posicionesabiertas.PosicionesAbiertasTestMother;
import es.serversurvival.v1.jugadores.JugadoresTestMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static es.serversurvival.v1.MockitoArgEqualsMatcher.*;
import static es.serversurvival.v1.bolsa.posicionesabiertas.PosicionesAbiertasTestMother.createPosicionAbierta;
import static es.serversurvival.v1.bolsa.ordenespremarket.OrdernesPremarketTestMother.createOrdenPremarket;
import static es.serversurvival.v1.jugadores.JugadoresTestMother.createJugador;
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
        PosicionAbierta posicionAbierta = PosicionesAbiertasTestMother.createPosicionAbierta("jaime", "AMZN").withCantidad(4);
        OrdenPremarket ordenVentaLargo = createOrdenPremarket("jaime", "AMZN", posicionAbierta.getPosicionAbiertaId(),
                TipoAccion.CORTO_COMPRA, 200);
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
        PosicionAbierta posicionAbierta = PosicionesAbiertasTestMother.createPosicionAbierta("jaime", "AMZN").withCantidad(4);
        OrdenPremarket ordenVentaLargo = createOrdenPremarket("jaime", "AMZN", posicionAbierta.getPosicionAbiertaId(), TipoAccion.CORTO_COMPRA, 2);
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
        OrdenPremarket ordenCompraLargo = createOrdenPremarket("jaime", "AMZN", null, TipoAccion.CORTO_VENTA, 2);
        Jugador jugador = JugadoresTestMother.createJugador("jaime", 0);
        ActivoInfo activoInfo = ActivosInfoTestMother.createActivoInfoAcciones("AMZN").withPrecio(20).withNombreActivoLargo("Amazon");
        when(this.ordenesPremarketService.findAll()).thenReturn(List.of(ordenCompraLargo));
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(jugador);
        when(this.activoInfoService.getByNombreActivo("AMZN", TipoActivo.ACCIONES)).thenReturn(activoInfo);

        this.useCase.ejecutarOrdenes();

        verify(this.eventBus, times(1)).publish(argThat(of(
                OrdenNoEjecutadoEvento.of("jaime", "AMZN", 2)
        )));
        verify(this.ordenesPremarketService, times(1)).deleteById(ordenCompraLargo.getOrderPremarketId());
    }

    @Test
    public void ventaCortoExecute(){
        OrdenPremarket ordenCompraLargo = createOrdenPremarket("jaime", "AMZN", null, TipoAccion.CORTO_VENTA, 2);
        Jugador jugador = JugadoresTestMother.createJugador("jaime", 100);
        ActivoInfo activoInfo = ActivosInfoTestMother.createActivoInfoAcciones("AMZN").withPrecio(20).withNombreActivoLargo("Amazon");
        when(this.ordenesPremarketService.findAll()).thenReturn(List.of(ordenCompraLargo));
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(jugador);
        when(this.activoInfoService.getByNombreActivo("AMZN", TipoActivo.ACCIONES)).thenReturn(activoInfo);

        this.useCase.ejecutarOrdenes();

        ArgumentCaptor<Integer> cantidadArgCapturer = ArgumentCaptor.forClass(int.class);
        verify(this.venderCortoUseCase, times(1)).venderEnCortoBolsa(
                argThat(of("jaime")),
                argThat(of("AMZN")),
                cantidadArgCapturer.capture()
        );
        assertThat(cantidadArgCapturer.getValue()).isEqualTo(ordenCompraLargo.getCantidad());
        verify(this.ordenesPremarketService, times(1)).deleteById(ordenCompraLargo.getOrderPremarketId());
    }

    @Test
    public void ventaLargoNotExecuted(){
        PosicionAbierta posicionAbierta = PosicionesAbiertasTestMother.createPosicionAbierta("jaime", "AMZN").withCantidad(1);
        OrdenPremarket ordenCompraLargo = createOrdenPremarket("jaime", "AMZN", posicionAbierta.getPosicionAbiertaId(), TipoAccion.LARGO_VENTA, 4);
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
        PosicionAbierta posicionAbierta = PosicionesAbiertasTestMother.createPosicionAbierta("jaime", "AMZN").withCantidad(4);
        OrdenPremarket ordenVentaLargo = createOrdenPremarket("jaime", "AMZN", posicionAbierta.getPosicionAbiertaId(), TipoAccion.LARGO_VENTA, 2);
        when(this.ordenesPremarketService.findAll()).thenReturn(List.of(ordenVentaLargo));
        when(this.posicionesAbiertasSerivce.getById(posicionAbierta.getPosicionAbiertaId())).thenReturn(posicionAbierta);

        this.useCase.ejecutarOrdenes();

        ArgumentCaptor<Integer> quantityArgumentCapturer = ArgumentCaptor.forClass(Integer.class);
        verify(this.venderLargoUseCase, times(1)).venderPosicion(
                argThat(of(posicionAbierta.getPosicionAbiertaId())),
                quantityArgumentCapturer.capture(),
                argThat(of("jaime"))
        );
        assertThat(quantityArgumentCapturer.getValue()).isEqualTo(ordenVentaLargo.getCantidad());
        verify(this.ordenesPremarketService, times(1)).deleteById(ordenVentaLargo.getOrderPremarketId());
    }

    @Test
    public void compraLargoNotExecuted(){
        OrdenPremarket ordenCompraLargo = createOrdenPremarket("jaime", "AMZN", null, TipoAccion.LARGO_COMPRA, 2);
        Jugador jugador = JugadoresTestMother.createJugador("jaime", 0);
        ActivoInfo activoInfo = ActivosInfoTestMother.createActivoInfoAcciones("AMZN").withPrecio(20);
        when(this.ordenesPremarketService.findAll()).thenReturn(List.of(ordenCompraLargo));
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(jugador);
        when(this.activoInfoService.getByNombreActivo("AMZN", TipoActivo.ACCIONES)).thenReturn(activoInfo);

        this.useCase.ejecutarOrdenes();

        verify(this.eventBus, times(1)).publish(argThat(of(
                OrdenNoEjecutadoEvento.of("jaime", "AMZN", 2)
        )));
        verify(this.ordenesPremarketService, times(1)).deleteById(ordenCompraLargo.getOrderPremarketId());
    }

    @Test
    public void compraLargoExecute(){
        OrdenPremarket ordenCompraLargo = createOrdenPremarket("jaime", "AMZN", null, TipoAccion.LARGO_COMPRA, 2);
        Jugador jugador = JugadoresTestMother.createJugador("jaime", 100);
        ActivoInfo activoInfo = ActivosInfoTestMother.createActivoInfoAcciones("AMZN").withPrecio(20);
        when(this.ordenesPremarketService.findAll()).thenReturn(List.of(ordenCompraLargo));
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(jugador);
        when(this.activoInfoService.getByNombreActivo("AMZN", TipoActivo.ACCIONES)).thenReturn(activoInfo);

        this.useCase.ejecutarOrdenes();

        ArgumentCaptor<Integer> cantidadArgCapturer = ArgumentCaptor.forClass(int.class);
        verify(this.comprarLargoUseCase, times(1)).comprarLargo(
                argThat(of("jaime")),
                argThat(of(TipoActivo.ACCIONES)),
                argThat(of("AMZN")),
                cantidadArgCapturer.capture()
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
