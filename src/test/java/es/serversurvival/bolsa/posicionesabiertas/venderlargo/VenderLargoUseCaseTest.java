package es.serversurvival.bolsa.posicionesabiertas.venderlargo;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.jugadores.JugadoresTestMother;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static es.serversurvival.MockitoArgEqualsMatcher.of;
import static es.serversurvival.bolsa.activosinfo.ActivosInfoTestMother.createActivoInfoAcciones;
import static es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo.*;
import static es.serversurvival.bolsa.posicionesabiertas.PosicionesAbiertasTestMother.createPosicionAbierta;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class VenderLargoUseCaseTest {
    @Mock private PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    @Mock private JugadoresService jugadoresService;
    @Mock private ActivosInfoService activoInfoService;
    @Mock private EventBus eventBus;
    private VenderLargoUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new VenderLargoUseCase(
                this.posicionesAbiertasSerivce,
                this.jugadoresService,
                this.activoInfoService,
                this.eventBus
        );
    }

    @Test
    public void shouldVenderLargoPartial(){
        var activoInfo = createActivoInfoAcciones("AMZN").withNombreActivoLargo("Amazon").withPrecio(20);
        when(this.activoInfoService.getByNombreActivo("AMZN", ACCIONES)).thenReturn(activoInfo);
        var posicionAbierta = createPosicionAbierta("jaime", "AMZN").withCantidad(2);
        when(this.posicionesAbiertasSerivce.getById(posicionAbierta.getPosicionAbiertaId())).thenReturn(posicionAbierta);
        Jugador jugador = JugadoresTestMother.createJugador("jaime", 1000);
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(jugador);

        double valorTotal = activoInfo.getPrecio() * (posicionAbierta.getCantidad() - 1);

        this.useCase.venderPosicion(posicionAbierta.getPosicionAbiertaId(), posicionAbierta.getCantidad() - 1, "jaime");

        verify(this.posicionesAbiertasSerivce, times(1)).save(posicionAbierta.withCantidad(posicionAbierta.getCantidad() - 1));
        verify(this.jugadoresService, times(1)).save(argThat(of(
                jugador.incrementPixelcoinsBy(valorTotal).incrementIngresosBy(valorTotal)
        )));
        verify(this.eventBus, times(1)).publish(argThat(of(new PosicionVentaLargoEvento(
                "jaime", "AMZN", "Amazon", posicionAbierta.getPrecioApertura(), posicionAbierta.getFechaApertura(),
                activoInfo.getPrecio(), posicionAbierta.getCantidad() - 1, activoInfo.getTipoActivo()
        ))));
    }

    @Test
    public void shouldVenderLargoTotal(){
        var activoInfo = createActivoInfoAcciones("AMZN").withNombreActivoLargo("Amazon").withPrecio(20);
        when(this.activoInfoService.getByNombreActivo("AMZN", ACCIONES)).thenReturn(activoInfo);
        var posicionAbierta = createPosicionAbierta("jaime", "AMZN");
        when(this.posicionesAbiertasSerivce.getById(posicionAbierta.getPosicionAbiertaId())).thenReturn(posicionAbierta);
        Jugador jugador = JugadoresTestMother.createJugador("jaime", 1000);
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(jugador);

        double valorTotal = activoInfo.getPrecio() * posicionAbierta.getCantidad();

        this.useCase.venderPosicion(posicionAbierta.getPosicionAbiertaId(), posicionAbierta.getCantidad(), "jaime");

        verify(this.posicionesAbiertasSerivce, times(1)).deleteById(posicionAbierta.getPosicionAbiertaId());
        verify(this.jugadoresService, times(1)).save(argThat(of(
                jugador.incrementPixelcoinsBy(valorTotal).incrementIngresosBy(valorTotal)
        )));
        verify(this.eventBus, times(1)).publish(argThat(of(new PosicionVentaLargoEvento(
                "jaime", "AMZN", "Amazon", posicionAbierta.getPrecioApertura(), posicionAbierta.getFechaApertura(),
                activoInfo.getPrecio(), posicionAbierta.getCantidad(), activoInfo.getTipoActivo()
        ))));
    }

    @Test
    public void correctQuantity(){
        var posicionAbierta = createPosicionAbierta("jaime", "AMZN");
        when(this.posicionesAbiertasSerivce.getById(posicionAbierta.getPosicionAbiertaId())).thenReturn(posicionAbierta);

        assertThatCode(() -> this.useCase.venderPosicion(posicionAbierta.getPosicionAbiertaId(), -1, "jaime"))
                .isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.venderPosicion(posicionAbierta.getPosicionAbiertaId(), 0, "jaime"))
                .isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.venderPosicion(posicionAbierta.getPosicionAbiertaId(), posicionAbierta.getCantidad() + 1,
                "jaime"))
                .isInstanceOf(IllegalQuantity.class);
    }

    @Test
    public void ownerOfPosicion(){
        var posicionAbierta = createPosicionAbierta("otro", "AMZN");
        when(this.posicionesAbiertasSerivce.getById(posicionAbierta.getPosicionAbiertaId())).thenReturn(posicionAbierta);
        assertThatCode(() -> this.useCase.venderPosicion(posicionAbierta.getPosicionAbiertaId(), 1, "jaime"))
                .isInstanceOf(NotTheOwner.class);
    }

    @Test
    public void posicionAbiertaExsits(){
        var posicionAbiertaId = UUID.randomUUID();
        when(this.posicionesAbiertasSerivce.getById(posicionAbiertaId)).thenThrow(ResourceNotFound.class);
        assertThatCode(() -> this.useCase.venderPosicion(posicionAbiertaId, 1, null))
                .isInstanceOf(ResourceNotFound.class);
    }

}
