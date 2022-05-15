package es.serversurvival.jugadores.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.TipoAccionista;
import es.serversurvival.empresas.ofertasaccionesserver.comprarofertasmercadoserver.EmpresaServerAccionCompradaEvento;
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
        OfertaAccionServer oferta = evento.getOferta();
        Jugador comprador = jugadoresService.getByNombre(evento.getJugador());

        jugadoresService.save(comprador.decrementPixelcoinsBy(evento.getPixelcoins()));

        if(oferta.getTipoOfertante() == TipoAccionista.JUGADOR){
            Jugador jugadorVendedor = jugadoresService.getByNombre(oferta.getNombreOfertante());
            double beneficiosPerdidas = (oferta.getPrecio() - oferta.getPrecioApertura()) * evento.getCantidad();
            double rentabilidad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(oferta.getPrecioApertura(), oferta.getPrecio()), 3);

            if(beneficiosPerdidas >= 0)
                jugadoresService.save(jugadorVendedor.incrementPixelcoinsBy(evento.getPixelcoins()).incrementGastosBy(beneficiosPerdidas));
            else
                jugadoresService.save(jugadorVendedor.incrementPixelcoinsBy(evento.getPixelcoins()).incrementIngresosBy(beneficiosPerdidas));
        }
    }
}
