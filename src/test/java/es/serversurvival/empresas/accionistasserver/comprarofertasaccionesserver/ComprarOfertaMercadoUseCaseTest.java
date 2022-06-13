package es.serversurvival.empresas.accionistasserver.comprarofertasaccionesserver;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.empresas.accionistasserver._shared.domain.AccionistaServer;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.empresas.ofertasaccionesserver.comprarofertasaccionesserver.ComprarOfertaMercadoUseCase;
import es.serversurvival.empresas.ofertasaccionesserver.comprarofertasaccionesserver.EmpresaServerAccionComprada;
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
import static es.serversurvival.empresas.accionistasserver.AccionistasServerTestMother.createAccionistaTipoEmpresa;
import static es.serversurvival.empresas.accionistasserver.AccionistasServerTestMother.createAccionnistaTipoJugaodor;
import static es.serversurvival.empresas.accionistasserver._shared.domain.TipoAccionista.*;
import static es.serversurvival.empresas.empresas.EmpresasTestMother.createEmpresa;
import static es.serversurvival.empresas.ofertasaccionesserver.OfertasAccionesServerTestMother.createOfertAccionServerEmpresa;
import static es.serversurvival.empresas.ofertasaccionesserver.OfertasAccionesServerTestMother.createOfertAccionServerJugador;
import static es.serversurvival.jugadores.JugadoresTestMother.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public final class ComprarOfertaMercadoUseCaseTest {
    @Mock private JugadoresService jugadoresService;
    @Mock private OfertasAccionesServerService ofertasAccionesServerService;
    @Mock private AccionistasServerService accionistasServerService;
    @Mock private EmpresasService empresasService;
    @Mock private EventBus eventBus;
    private ComprarOfertaMercadoUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new ComprarOfertaMercadoUseCase(
                this.jugadoresService,
                this.ofertasAccionesServerService,
                this.accionistasServerService,
                this.empresasService,
                this.eventBus
        );
    }

    @Test
    public void shouldComprarPartEmpresa(){
        UUID idToTest = UUID.randomUUID();
        OfertaAccionServer oferta = createOfertAccionServerEmpresa("empresa", "empresa", 20, 2);
        Empresa empresa = createEmpresa("empresa", "jaime");
        Jugador comprador = createJugador("comprador", 50);
        AccionistaServer accionistaVendedor = createAccionistaTipoEmpresa("empresa", "empresa", 2);
        when(this.jugadoresService.getByNombre("comprador")).thenReturn(comprador);
        when(this.ofertasAccionesServerService.getById(idToTest)).thenReturn(oferta);
        when(this.empresasService.getByNombre("empresa")).thenReturn(empresa);
        when(this.accionistasServerService.getById(oferta.getAccionistaEmpresaServerId())).thenReturn(accionistaVendedor);

        this.useCase.comprarOfertaMercadoAccionServer("comprador", idToTest, 1);

        verify(this.ofertasAccionesServerService, times(1)).save(argThat(of(
                oferta.decreaseCantidadBy(1)
        )));
        verify(this.accionistasServerService, times(1)).save(
                argThat(of("comprador")), argThat(of(JUGADOR)), argThat(of("empresa")), anyInt(), anyDouble()
        );
        verify(this.accionistasServerService, times(1)).save(argThat(of(
                accionistaVendedor.decreaseCantidad(1)
        )));
        verify(this.jugadoresService, times(1)).save(argThat(of(
                comprador.decrementPixelcoinsBy(20).incrementGastosBy(20)
        )));
        verify(this.empresasService, times(1)).save(argThat(of(
                empresa.incrementPixelcoinsBy(20)
        )));
        verify(this.eventBus, times(1)).publish(argThat(of(
                EmpresaServerAccionComprada.of("comprador", "empresa", EMPRESA, "empresa", 20, 1)
        )));
    }

    @Test
    public void shouldComprarFullJugador(){
        UUID idToTest = UUID.randomUUID();
        OfertaAccionServer oferta = createOfertAccionServerJugador("vendedor", "empresa", 20, 2);
        Jugador comprador = createJugador("comprador", 50);
        Jugador vendedor = createJugador("vendedor", 50);
        AccionistaServer accionistaVendedor = createAccionnistaTipoJugaodor("vendedor", "empresa", 2);
        when(this.jugadoresService.getByNombre("comprador")).thenReturn(comprador);
        when(this.jugadoresService.getByNombre("vendedor")).thenReturn(vendedor);
        when(this.ofertasAccionesServerService.getById(idToTest)).thenReturn(oferta);
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "jaime"));
        when(this.accionistasServerService.getById(oferta.getAccionistaEmpresaServerId())).thenReturn(accionistaVendedor);

        this.useCase.comprarOfertaMercadoAccionServer("comprador", idToTest, 2);

        verify(this.ofertasAccionesServerService, times(1)).deleteById(oferta.getOfertaAccionServerId());
        verify(this.accionistasServerService, times(1)).save(
                argThat(of("comprador")), argThat(of(JUGADOR)), argThat(of("empresa")), anyInt(), anyDouble()
        );
        verify(this.accionistasServerService, times(1)).deleteById(accionistaVendedor.getAccionistaServerId());
        verify(this.jugadoresService, times(1)).save(argThat(of(
                comprador.decrementPixelcoinsBy(20 * 2).incrementGastosBy(20 * 2)
        )));
        verify(this.jugadoresService, times(1)).save(argThat(of(
                vendedor.incrementPixelcoinsBy(20 * 2).incrementIngresosBy((20 * 2)
        ))));
        verify(this.eventBus, times(1)).publish(argThat(of(
                EmpresaServerAccionComprada.of("comprador", "vendedor", JUGADOR, "empresa", 20 * 2, 2)
        )));
    }

    @Test
    public void enoughPixelcoins(){
        UUID idToTest = UUID.randomUUID();
        OfertaAccionServer oferta = createOfertAccionServerJugador("vendedor", "empresa", 20, 10);
        Jugador comprador = createJugador("comprador", 100);
        when(this.jugadoresService.getByNombre("comprador")).thenReturn(comprador);
        when(this.ofertasAccionesServerService.getById(idToTest)).thenReturn(oferta);

        assertThatCode(() -> this.useCase.comprarOfertaMercadoAccionServer("comprador", idToTest, 10))
                .isInstanceOf(NotEnoughPixelcoins.class);
    }

    @Test
    public void notHisSelf(){
        UUID idToTest = UUID.randomUUID();
        when(this.jugadoresService.getByNombre("comprador")).thenReturn(createJugador("comprador"));
        when(this.ofertasAccionesServerService.getById(idToTest)).thenReturn(createOfertAccionServerJugador("comprador", "!2"));

        assertThatCode(() -> this.useCase.comprarOfertaMercadoAccionServer("comprador", idToTest, 1))
                .isInstanceOf(CannotBeYourself.class);
    }

    @Test
    public void cantidadCorrectQuantity(){
        UUID idToTest = UUID.randomUUID();
        OfertaAccionServer oferta = createOfertAccionServerJugador("comprador", ":)");
        when(this.jugadoresService.getByNombre("comprador")).thenReturn(createJugador("comprador"));
        when(this.ofertasAccionesServerService.getById(idToTest)).thenReturn(oferta);

        assertThatCode(() -> this.useCase.comprarOfertaMercadoAccionServer("comprador", idToTest, -1))
                .isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.comprarOfertaMercadoAccionServer("comprador", idToTest, 0))
                .isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.comprarOfertaMercadoAccionServer("comprador", idToTest, oferta.getCantidad() + 1))
                .isInstanceOf(IllegalQuantity.class);
    }

    @Test
    public void ofertaExists(){
        UUID idToTest = UUID.randomUUID();
        when(this.ofertasAccionesServerService.getById(idToTest)).thenThrow(ResourceNotFound.class);
        assertThatCode(() -> this.useCase.comprarOfertaMercadoAccionServer("lkajs", idToTest, 1))
                .isInstanceOf(ResourceNotFound.class);
    }
}
