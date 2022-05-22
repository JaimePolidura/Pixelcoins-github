package es.serversurvival.bolsa.activosinfo.eventlisteners;

import es.serversurvival.MockitoArgEqualsMatcher;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.ordenespremarket.ejecutarordenes.OrdenNoEjecutadoEvento;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static es.serversurvival.MockitoArgEqualsMatcher.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class OnOrdenPremarketNoEjecutada {
    @Mock private ActivosInfoService activoInfoService;
    @Mock private PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private OnPremarketOrdenNoEjecutada usecase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.usecase = new OnPremarketOrdenNoEjecutada(
                this.activoInfoService,
                this.posicionesAbiertasSerivce
        );
    }

    @Test
    public void existsInPosicionesAbiertas(){
        when(this.posicionesAbiertasSerivce.existsByNombreActivo("AMZN")).thenReturn(true);
        this.usecase.onOrdenNoEjecutada(OrdenNoEjecutadoEvento.of("jaime", "AMZN", 1));

        verify(this.activoInfoService, never()).deleteByNombreActivo(any(String.class));
    }

    @Test
    public void notExistsInPosicionesAbiertas(){
        when(this.posicionesAbiertasSerivce.existsByNombreActivo("AMZN")).thenReturn(false);
        this.usecase.onOrdenNoEjecutada(OrdenNoEjecutadoEvento.of("jaime", "AMZN", 1));

        verify(this.activoInfoService, times(1)).deleteByNombreActivo(argThat(of("AMZN")));
    }
}
