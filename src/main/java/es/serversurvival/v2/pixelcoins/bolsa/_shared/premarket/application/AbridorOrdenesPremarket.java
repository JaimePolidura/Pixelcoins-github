package es.serversurvival.v2.pixelcoins.bolsa._shared.premarket.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.EventBus;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v2.pixelcoins.bolsa._shared.BolsaValidator;
import es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones.Posicion;
import es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones.PosicionesService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.premarket.domain.OrdenPremarket;
import es.serversurvival.v2.pixelcoins.bolsa._shared.premarket.domain.OrdenPremarketAbierta;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class AbridorOrdenesPremarket {
    private final OrdenesPremarketService ordenesPremarketService;
    private final PosicionesService posicionesService;
    private final BolsaValidator validator;
    private final EventBus eventBus;

    public boolean estaElMercadoAbierto() {
        return !Funciones.esHoyDiaSemana(7, 1) && Funciones.esHoyHora(15, 30, 22, 30);
    }

    public void abrirOrdenCerrar(AbrirOrdenPremarketCerrarParametros parametros) {
        validator.ordenesCerrarCantidadNoMayorPosicion(parametros.getPosicionId(), parametros.getCantidadACerrar());

        Posicion posicion = posicionesService.getById(parametros.getPosicionId());

        UUID ordenId = ordenesPremarketService.save(OrdenPremarket.cerrarBuilder()
                .tipoBolsaApuesta(posicion.getTipoApuesta())
                .posicionAbiertaId(parametros.getPosicionId())
                .cantidad(parametros.getCantidadACerrar())
                .jugadorId(posicion.getJugadorId())
                .build());

        eventBus.publish(new OrdenPremarketAbierta(ordenId));
    }

    public void abrirOrdenAbrir(AbrirOrdenPremarketAbrirParametros parametros) {
        UUID ordenId = ordenesPremarketService.save(OrdenPremarket.abrirBuilder()
                .tipoBolsaApuesta(parametros.getTipoBolsaApuesta())
                .cantidad(parametros.getCantidad())
                .jugadorId(parametros.getJugadorId())
                .activoBolsaId(parametros.getActivoBolsaId())
                .build());

        eventBus.publish(new OrdenPremarketAbierta(ordenId));
    }
}
