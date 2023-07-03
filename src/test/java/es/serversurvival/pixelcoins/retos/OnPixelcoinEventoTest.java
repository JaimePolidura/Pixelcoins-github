package es.serversurvival.pixelcoins.retos;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetosService;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.Reto;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.RetoProgresivoService;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.TipoReto;
import es.serversurvival.pixelcoins.retos._shared.retosadquiridos.application.AdquisitorRetos;
import es.serversurvival.pixelcoins.retos._shared.retosadquiridos.application.RetosAdquiridosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static es.serversurvival.pixelcoins.retos._shared.retos.domain.TipoReto.*;
import static org.mockito.Mockito.*;

public class OnPixelcoinEventoTest {
    private OnPixelcoinEvento toTest;
    private DependenciesRepository dependenciesRepository;
    private RetosAdquiridosService retosAdquiridosService;
    private AdquisitorRetos adquisitorRetos;
    private RetosService retosService;

    @BeforeEach
    public void setup() {
        this.dependenciesRepository = mock(DependenciesRepository.class);
        this.retosAdquiridosService = mock(RetosAdquiridosService.class);
        this.adquisitorRetos = mock(AdquisitorRetos.class);
        this.retosService = mock(RetosService.class);
        this.toTest = new OnPixelcoinEvento(dependenciesRepository, retosAdquiridosService, adquisitorRetos, retosService);
    }

    @Test
    public void retoProgresivo_seAdquire() {
        UUID jugadorId = UUID.randomUUID();
        UUID otro = UUID.randomUUID();
        UUID retoIdPadreProgresion = UUID.randomUUID();
        double cantidadActualJugador = 1200.0d;
        int retoIdPadre = RetoMapping.BOLSA_ABRIR_LARGO.getRetoId();

        RetoProgresivoService retoProgresivoService = mock(RetoProgresivoService.class);
        Reto retoPrimero = Reto.builder().cantidadRequerida(100.0d).retoId(1).retoPadreProgresionId(retoIdPadreProgresion).tipo(PROGRESIVO).build();
        Reto retoSegundo = Reto.builder().cantidadRequerida(500.0d).retoId(2).retoPadreProgresionId(retoIdPadreProgresion).tipo(PROGRESIVO).build();
        Reto retoTercero = Reto.builder().cantidadRequerida(1000.0d).retoId(3).retoPadreProgresionId(retoIdPadreProgresion).tipo(PROGRESIVO).build();
        Reto retoCuarto = Reto.builder().cantidadRequerida(2000.0d).retoId(4).retoPadreProgresionId(retoIdPadreProgresion).tipo(PROGRESIVO).build();

        when(dependenciesRepository.get(any())).thenReturn(retoProgresivoService);
        when(retosService.findByRetoLineaPadre(retoIdPadreProgresion)).thenReturn(List.of(retoPrimero, retoSegundo, retoTercero, retoCuarto));
        when(retosAdquiridosService.estaAdquirido(jugadorId, retoPrimero.getRetoId())).thenReturn(true);
        when(retosAdquiridosService.estaAdquirido(jugadorId, retoIdPadre)).thenReturn(false);
        when(retoProgresivoService.getCantidad(jugadorId, otro)).thenReturn(cantidadActualJugador);
        when(retosService.getById(retoIdPadre)).thenReturn(retoPrimero);

        InvocaAUnReto eventoMock = mock(InvocaAUnReto.class);
        when(eventoMock.otroDatoReto()).thenReturn(otro);
        when(eventoMock.retosByJugadorId()).thenReturn(Map.of(jugadorId, RetoMapping.BOLSA_ABRIR_LARGO));

        toTest.on(eventoMock);

        verify(adquisitorRetos, times(1)).adquirir(jugadorId, List.of(2, 3));
    }

    @Test
    public void retoProgresivo_noSeAdquire() {
        UUID jugadorId = UUID.randomUUID();
        UUID retoIdPadreProgresion = UUID.randomUUID();
        UUID otro = UUID.randomUUID();
        double cantidadActualJugador = 200.0d;
        int retoIdPadre = RetoMapping.BOLSA_ABRIR_LARGO.getRetoId();

        RetoProgresivoService retoProgresivoService = mock(RetoProgresivoService.class);
        Reto retoPrimero = Reto.builder().cantidadRequerida(100.0d).retoId(1).retoPadreProgresionId(retoIdPadreProgresion).tipo(PROGRESIVO).build();
        Reto retoSegundo = Reto.builder().cantidadRequerida(500.0d).retoId(2).retoPadreProgresionId(retoIdPadreProgresion).tipo(PROGRESIVO).build();
        Reto retoTercero = Reto.builder().cantidadRequerida(1000.0d).retoId(3).retoPadreProgresionId(retoIdPadreProgresion).tipo(PROGRESIVO).build();

        when(dependenciesRepository.get(any())).thenReturn(retoProgresivoService);
        when(retosService.findByRetoLineaPadre(retoIdPadreProgresion)).thenReturn(List.of(retoPrimero, retoSegundo, retoTercero));
        when(retosAdquiridosService.estaAdquirido(jugadorId, retoPrimero.getRetoId())).thenReturn(true);
        when(retosAdquiridosService.estaAdquirido(jugadorId, retoIdPadre)).thenReturn(false);
        when(retoProgresivoService.getCantidad(jugadorId, otro)).thenReturn(cantidadActualJugador);
        when(retosService.getById(retoIdPadre)).thenReturn(retoPrimero);

        InvocaAUnReto eventoMock = mock(InvocaAUnReto.class);
        when(eventoMock.otroDatoReto()).thenReturn(otro);
        when(eventoMock.retosByJugadorId()).thenReturn(Map.of(jugadorId, RetoMapping.BOLSA_ABRIR_LARGO));

        toTest.on(eventoMock);

        verify(adquisitorRetos, never()).adquirir(any(UUID.class), any(List.class));
    }

    @Test
    public void retoIndependiente() {
        UUID jugadorId = UUID.randomUUID();

        Reto reto = mock(Reto.class);

        when(reto.getTipo()).thenReturn(INDEPENDIENTE);
        when(retosAdquiridosService.estaAdquirido(jugadorId, RetoMapping.BOLSA_ABRIR_LARGO.getRetoId())).thenReturn(false);
        when(retosService.getById(RetoMapping.BOLSA_ABRIR_LARGO.getRetoId())).thenReturn(reto);

        toTest.on(() -> Map.of(jugadorId, RetoMapping.BOLSA_ABRIR_LARGO));

        verify(adquisitorRetos, times(1)).adquirir(jugadorId, RetoMapping.BOLSA_ABRIR_LARGO.getRetoId());
    }

    @Test
    public void retoAdquirido() {
        UUID jugadorId = UUID.randomUUID();

        when(retosAdquiridosService.estaAdquirido(jugadorId, RetoMapping.BOLSA_ABRIR_LARGO.getRetoId())).thenReturn(true);

        toTest.on(() -> Map.of(jugadorId, RetoMapping.BOLSA_ABRIR_LARGO));

        verify(adquisitorRetos, never()).adquirir(any(UUID.class), anyInt());
    }

    @Test
    public void noHayRetos() {
        toTest.on(() -> null);
        verify(adquisitorRetos, never()).adquirir(any(UUID.class), anyInt());

        toTest.on(() -> Collections.EMPTY_MAP);
        verify(adquisitorRetos, never()).adquirir(any(UUID.class), anyInt());
    }
}
