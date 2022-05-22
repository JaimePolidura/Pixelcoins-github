package es.serversurvival.bolsa.activosinfo.eventlisteners;

import es.serversurvival.MockitoArgEqualsMatcher;
import es.serversurvival.bolsa.activosinfo.DepencyContainerTipoActivoInfoMocks;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbiertaEvento;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static es.serversurvival.bolsa.activosinfo.DepencyContainerTipoActivoInfoMocks.accionesApiService;
import static es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo.*;
import static es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion.CORTO;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class OnPosicionAbiertaTest {
    @Mock private ActivosInfoService activoInfoService;
    private OnPosicionAbierta useCase;

    static  {
        DepencyContainerTipoActivoInfoMocks.loadDepencyContainer();
    }

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new OnPosicionAbierta(activoInfoService);
    }

    @Test
    @SneakyThrows
    public void onPosicionAbiertaNotExistsWithoutNombreActivoLargo(){
        when(this.activoInfoService.existsByNombreActivo("AMZN")).thenReturn(false);
        when(accionesApiService.getNombreActivoLargo("AMZN")).thenReturn("tumadre");

        this.useCase.onOpenedPosition(of("AMZN", null, 10));

        verify(this.activoInfoService, times(1)).save(argThat(MockitoArgEqualsMatcher.of(
                new ActivoInfo("AMZN", 10, ACCIONES, "tumadre")
        )));
    }

    @Test
    public void onPosicionAbiertaNotExistsWithNombreActivoLargo(){
        when(this.activoInfoService.existsByNombreActivo("AMZN")).thenReturn(false);
        this.useCase.onOpenedPosition(of("AMZN", "Amazon", 10));

        verify(this.activoInfoService, times(1)).save(argThat(MockitoArgEqualsMatcher.of(
                new ActivoInfo("AMZN", 10, ACCIONES, "Amazon")
        )));
    }

    @Test
    public void onPosicionAbiertaAlreadyExists(){
        when(this.activoInfoService.existsByNombreActivo("AMZN")).thenReturn(true);
        this.useCase.onOpenedPosition(of("AMZN", "Amazon", 10));

        verify(this.activoInfoService, never()).save(any(ActivoInfo.class));
    }

    private PosicionAbiertaEvento of(String nombreActivo, String nombreActivoLargo, double precioUnidad){
        return PosicionAbiertaEvento.of("jaime", nombreActivo, 1, precioUnidad, ACCIONES,
                20, nombreActivoLargo, CORTO);
    }
}
