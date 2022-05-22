package es.serversurvival.bolsa.activosinfo.actualizar;

import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static es.serversurvival.bolsa.activosinfo.ActivosInfoTestMother.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ActualizarActivosInfoUseCaseTest {
    @Mock private ActivosInfoService activoInfoService;
    private ActualizarActivosInfoUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new ActualizarActivosInfoUseCase(
                this.activoInfoService,
                new AtomicBoolean(false)
        );
        when(this.activoInfoService.findAll()).thenReturn(List.of(
                createActivoInfoAcciones("AMZN"),
                createActivoInfoCriptos("BTC"),
                createActivoInfoMateriasPrimas("OIL")
        ));
    }

    @Test
    public void shouldUpdate(){

        this.useCase.actualizrar();
    }
}
