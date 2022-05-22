package es.serversurvival.bolsa.activosinfo.eventlisteners;

import es.serversurvival.MockitoArgEqualsMatcher;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas.comprarcorto.PosicionCompraCortoEvento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static es.serversurvival.MockitoArgEqualsMatcher.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class OnPosicionCerradatest {
    @Mock private ActivosInfoService activoInfoService;
    @Mock private PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private OnPosicionCerrada useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new OnPosicionCerrada(
                this.activoInfoService,
                this.posicionesAbiertasSerivce
        );
    }

    @Test
    public void existsInPosicionesAbiertas(){
        when(this.posicionesAbiertasSerivce.existsByNombreActivo("AMZN")).thenReturn(true);
        this.useCase.on(new PosicionCompraCortoEvento(null, "AMZN", null, 0, null, 1, 1, null));
        verify(this.activoInfoService, never()).deleteByNombreActivo(any(String.class));
    }

    @Test
    public void notExistsInPosicionesAbiertas(){
        when(this.posicionesAbiertasSerivce.existsByNombreActivo("AMZN")).thenReturn(false);
        this.useCase.on(new PosicionCompraCortoEvento(null, "AMZN", null, 0, null, 1, 1, null));

        verify(this.activoInfoService, times(1)).deleteByNombreActivo(argThat(of("AMZN")));
    }
}
