package es.serversurvival.nfs.jugadores.eventlisteners.bolsa;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.eventos.empresas.EmpresaServerAccionCompradaEvento;
import es.serversurvival.nfs.bolsa.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.nfs.bolsa.ofertasmercadoserver.mysql.TipoOfertante;
import es.serversurvival.nfs.jugadores.mySQL.Jugador;
import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;

import static es.serversurvival.nfs.utils.Funciones.diferenciaPorcntual;
import static es.serversurvival.nfs.utils.Funciones.redondeoDecimales;

public final class OnCompraAccionServer implements AllMySQLTablesInstances {
    @EventListener
    public void on (EmpresaServerAccionCompradaEvento evento) {
        OfertaMercadoServer oferta = evento.getOferta();
        Jugador comprador = jugadoresMySQL.getJugador(evento.getJugador());

        jugadoresMySQL.setPixelcoin(comprador.getNombre(), comprador.getPixelcoins() - evento.getPixelcoins());

        if(oferta.getTipo_ofertante() == TipoOfertante.JUGADOR){
            Jugador jugadorVendedor = jugadoresMySQL.getJugador(oferta.getJugador());
            double beneficiosPerdidas = (oferta.getPrecio() - oferta.getPrecio_apertura()) * evento.getCantidad();
            double rentabilidad = redondeoDecimales(diferenciaPorcntual(oferta.getPrecio_apertura(), oferta.getPrecio()), 3);

            if(beneficiosPerdidas >= 0)
                jugadoresMySQL.setEstadisticas(jugadorVendedor.getNombre(), jugadorVendedor.getPixelcoins() + evento.getPixelcoins(), jugadorVendedor.getNventas(),
                        jugadorVendedor.getIngresos(), jugadorVendedor.getGastos() + beneficiosPerdidas);
            else
                jugadoresMySQL.setEstadisticas(jugadorVendedor.getNombre(), jugadorVendedor.getPixelcoins() + evento.getPixelcoins(), jugadorVendedor.getNventas(),
                        jugadorVendedor.getIngresos() + beneficiosPerdidas, jugadorVendedor.getGastos());
        }
    }
}
