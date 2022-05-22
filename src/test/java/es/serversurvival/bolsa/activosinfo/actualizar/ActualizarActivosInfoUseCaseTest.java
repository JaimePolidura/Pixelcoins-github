package es.serversurvival.bolsa.activosinfo.actualizar;

import com.rabbitmq.tools.json.JSONUtil;
import es.serversurvival.MockitoArgEqualsMatcher;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.acciones.AccionesApiService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.criptomonedas.CriptomonedasApiService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.materiasprimas.MateriasPrimasApiService;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static es.serversurvival.bolsa.activosinfo.ActivosInfoTestMother.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public final class ActualizarActivosInfoUseCaseTest {
    @Mock private ActivosInfoService activoInfoService;
    private static final AccionesApiService accionesApiService;
    private static final MateriasPrimasApiService materiasPrimasApiService;
    private static final CriptomonedasApiService criptomonedasApiService;
    private ActualizarActivosInfoUseCase useCase;

    static  {
        accionesApiService = mock(AccionesApiService.class);
        materiasPrimasApiService = mock(MateriasPrimasApiService.class);
        criptomonedasApiService = mock(CriptomonedasApiService.class);

        DependecyContainer.add(AccionesApiService.class, accionesApiService);
        DependecyContainer.add(MateriasPrimasApiService.class, materiasPrimasApiService);
        DependecyContainer.add(CriptomonedasApiService.class, criptomonedasApiService);
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
        ActivoInfo accionTipoActivo = createActivoInfoAcciones("AMZN");
        ActivoInfo criptomonedaTipoActivo = createActivoInfoCriptos("BTC");
        ActivoInfo materiasPrimasTipoActivo = createActivoInfoMateriasPrimas("OIL");
        when(this.activoInfoService.findAll()).thenReturn(List.of(
                accionTipoActivo,
                criptomonedaTipoActivo,
                materiasPrimasTipoActivo
        ));

        when(this.accionesApiService.getPrecio("AMZN")).thenReturn(Double.valueOf(2100));
        when(this.accionesApiService.getNombreActivoLargo("AMZN")).thenReturn("AMAZON");
        when(this.criptomonedasApiService.getPrecio("BTC")).thenReturn(Double.valueOf(30000));
        when(this.criptomonedasApiService.getNombreActivoLargo("BTC")).thenReturn("BITCOIN");
        when(this.materiasPrimasApiService.getPrecio("OIL")).thenReturn(Double.valueOf(100));
        when(this.materiasPrimasApiService.getNombreActivoLargo("OIL")).thenReturn("Petroleo");

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
        Assertions.assertThat(this.useCase.isLoading()).isFalse();
    }
}
