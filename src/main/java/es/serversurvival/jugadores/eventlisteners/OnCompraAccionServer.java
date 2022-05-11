package es.serversurvival.jugadores.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.other._shared.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.bolsa.other._shared.ofertasmercadoserver.mysql.TipoOfertante;
import es.serversurvival.bolsa.other.comprarofertasmercadoserver.EmpresaServerAccionCompradaEvento;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival._shared.utils.Funciones;

public final class OnCompraAccionServer {
    private final JugadoresService jugadoresService;

    public OnCompraAccionServer() {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    @EventListener
    public void on (EmpresaServerAccionCompradaEvento evento) {
        OfertaMercadoServer oferta = evento.getOferta();
        Jugador comprador = jugadoresService.getByNombre(evento.getJugador());

        jugadoresService.save(comprador.decrementPixelcoinsBy(evento.getPixelcoins()));

        if(oferta.getTipo_ofertante() == TipoOfertante.JUGADOR){
            Jugador jugadorVendedor = jugadoresService.getByNombre(oferta.getJugador());
            double beneficiosPerdidas = (oferta.getPrecio() - oferta.getPrecio_apertura()) * evento.getCantidad();
            double rentabilidad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(oferta.getPrecio_apertura(), oferta.getPrecio()), 3);

            if(beneficiosPerdidas >= 0)
                jugadoresService.save(jugadorVendedor.incrementPixelcoinsBy(evento.getPixelcoins()).incrementGastosBy(beneficiosPerdidas));
            else
                jugadoresService.save(jugadorVendedor.incrementPixelcoinsBy(evento.getPixelcoins()).incrementIngresosBy(beneficiosPerdidas));
        }
    }
}
