package es.serversurvival.bolsa.activosinfo.actualizar;

import es.serversurvival.MockitoArgEqualsMatcher;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo.DepencyContainerTipoActivoInfoMocks;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static es.serversurvival.bolsa.activosinfo.ActivosInfoTestMother.*;
import static es.serversurvival.bolsa.activosinfo.DepencyContainerTipoActivoInfoMocks.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ActualizarActivosInfoUseCaseTest {
    @Mock private ActivosInfoService activoInfoService;
    private ActualizarActivosInfoUseCase useCase;

    static  {
        loadDepencyContainer();
    }

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new ActualizarActivosInfoUseCase(
                this.activoInfoService,
                new AtomicBoolean(false)
        );
    }

    @Test
    @SneakyThrows
    public void shouldUpdate(){
        loadDepencyContainer();

        when(accionesApiService.getPrecio("AMZN")).thenReturn(Double.valueOf(2100));
        when(accionesApiService.getNombreActivoLargo("AMZN")).thenReturn("AMAZON");
        when(criptomonedasApiService.getPrecio("BTC")).thenReturn(Double.valueOf(30000));
        when(criptomonedasApiService.getNombreActivoLargo("BTC")).thenReturn("BITCOIN");
        when(materiasPrimasApiService.getPrecio("OIL")).thenReturn(Double.valueOf(100));
        when(materiasPrimasApiService.getNombreActivoLargo("OIL")).thenReturn("Petroleo");

        ActivoInfo accionTipoActivo = createActivoInfoAcciones("AMZN");
        ActivoInfo criptomonedaTipoActivo = createActivoInfoCriptos("BTC");
        ActivoInfo materiasPrimasTipoActivo = createActivoInfoMateriasPrimas("OIL");
        when(this.activoInfoService.findAll()).thenReturn(List.of(
                accionTipoActivo,
                criptomonedaTipoActivo,
                materiasPrimasTipoActivo
        ));

        this.useCase.actualizrar();

        verify(this.activoInfoService, times(1)).save(argThat(MockitoArgEqualsMatcher.of(
                accionTipoActivo.withNombreActivoLargo("AMAZON").withPrecio(2100)
        )));
        verify(this.activoInfoService, times(1)).save(argThat(MockitoArgEqualsMatcher.of(
                criptomonedaTipoActivo.withNombreActivoLargo("BITCOIN").withPrecio(30000)
        )));
        verify(this.activoInfoService, times(1)).save(argThat(MockitoArgEqualsMatcher.of(
                materiasPrimasTipoActivo.withNombreActivoLargo("Petroleo").withPrecio(100)
        )));
        assertThat(this.useCase.isLoading()).isFalse();
    }
}
