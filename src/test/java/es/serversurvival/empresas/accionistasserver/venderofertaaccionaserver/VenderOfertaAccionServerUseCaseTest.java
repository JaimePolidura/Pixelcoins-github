package es.serversurvival.empresas.accionistasserver.venderofertaaccionaserver;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.MockitoArgEqualsMatcher;
import es.serversurvival.empresas.accionistasserver.AccionistasServerTestMother;
import es.serversurvival.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.empresas.accionistasserver._shared.domain.AccionistaServer;
import es.serversurvival.empresas.accionistasserver._shared.domain.TipoAccionista;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.empresas.ofertasaccionesserver.venderofertaaccionaserver.NuevaOfertaAccionServer;
import es.serversurvival.empresas.ofertasaccionesserver.venderofertaaccionaserver.VenderOfertaAccionServerUseCase;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static es.serversurvival.MockitoArgEqualsMatcher.of;
import static es.serversurvival.empresas.accionistasserver.AccionistasServerTestMother.createAccionnistaTipoJugaodor;
import static es.serversurvival.empresas.accionistasserver._shared.domain.TipoAccionista.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class VenderOfertaAccionServerUseCaseTest {
    @Mock private AccionistasServerService accionistasEmpresasServerService;
    @Mock private OfertasAccionesServerService ofertasAccionesServerService;
    @Mock private EventBus eventBus;
    private VenderOfertaAccionServerUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new VenderOfertaAccionServerUseCase(
                this.accionistasEmpresasServerService,
                this.ofertasAccionesServerService,
                this.eventBus
        );
    }

    @Test
    public void shouldVender(){
        AccionistaServer accionistaServer = createAccionnistaTipoJugaodor("jaime", "empresa", 1);
        UUID accionEmpresaServer = accionistaServer.getAccionEmpresaServerId();
        when(this.accionistasEmpresasServerService.getById(accionEmpresaServer)).thenReturn(accionistaServer);

        this.useCase.vender("jaime", accionEmpresaServer, 1, 1);

        verify(this.ofertasAccionesServerService, times(1)).save(
                argThat(of("jaime")), argThat(of("empresa")), anyDouble(), anyInt(), argThat(of(JUGADOR)), anyDouble(),
                argThat(of(accionistaServer.getAccionEmpresaServerId()))
        );

        verify(this.eventBus, times(1)).publish(argThat(of(
                NuevaOfertaAccionServer.of("jaime", "empresa", JUGADOR, 1, 1)
        )));
    }

    @Test
    public void ownsAccionEmpresaServer(){
        UUID accionEmpresaServer = UUID.randomUUID();
        when(this.accionistasEmpresasServerService.getById(accionEmpresaServer)).thenReturn(createAccionnistaTipoJugaodor("otro", "empresa", 1));

        assertThatCode(() -> this.useCase.vender("jaime", accionEmpresaServer, 1, 1))
                .isInstanceOf(NotTheOwner.class);
    }

    @Test
    public void cantidadNotBiggerThanAccionEmpresaServer(){
        UUID accionEmpresaServer = UUID.randomUUID();
        when(this.accionistasEmpresasServerService.getById(accionEmpresaServer)).thenReturn(createAccionnistaTipoJugaodor("otro", "empresa", 1));

        assertThatCode(() -> this.useCase.vender("sas", accionEmpresaServer, 1, 1000))
                .isInstanceOf(IllegalQuantity.class);
    }

    @Test
    public void accionEmpresaServerExists(){
        UUID accionEmpresaServer = UUID.randomUUID();
        when(this.accionistasEmpresasServerService.getById(accionEmpresaServer)).thenThrow(ResourceNotFound.class);
        assertThatCode(() -> this.useCase.vender("sas", accionEmpresaServer, 1, 1))
                .isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void cantidadPrecioCorrectFormat () {
        assertThatCode(() -> this.useCase.vender("sas", UUID.randomUUID(), -1, 1))
                .isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.vender("sas", UUID.randomUUID(), 0, 1))
                .isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.vender("sas", UUID.randomUUID(), 1, -1))
                .isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.vender("sas", UUID.randomUUID(), 1, 0))
                .isInstanceOf(IllegalQuantity.class);

    }

}
