package es.serversurvival.empresas.pagardividendos;

import es.serversurvival.bolsa.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.Pixelcoin;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public final class PagarDividendosEmpresaServerUseCase implements AllMySQLTablesInstances {
    public static final PagarDividendosEmpresaServerUseCase INSTANCE = new PagarDividendosEmpresaServerUseCase();

    private PagarDividendosEmpresaServerUseCase () {}

    public void pagarDividendoAccionServer (Player owner, String nombreEmpresa, double dividendoPorAccion, double totalAPagar) {
        List<PosicionAbierta> posicionesAccion = posicionesAbiertasMySQL.getPosicionesAccionesServer(nombreEmpresa);
        List<OfertaMercadoServer> ofertasAccion =  ofertasMercadoServerMySQL.getOfertasEmpresa(nombreEmpresa, OfertaMercadoServer::esTipoOfertanteJugador);

        Map<String, Jugador> allJugadoresMap = jugadoresMySQL.getMapAllJugadores();

        posicionesAccion.forEach(posicion -> {
            pagarDividendoAccionAJugador(allJugadoresMap.get(posicion.getJugador()), posicion.getCantidad(), dividendoPorAccion, nombreEmpresa);
        });
        ofertasAccion.forEach(oferta -> {
            pagarDividendoAccionAJugador(allJugadoresMap.get(oferta.getJugador()), oferta.getCantidad(), dividendoPorAccion, nombreEmpresa);
        });

        empresasMySQL.setPixelcoins(nombreEmpresa, empresasMySQL.getEmpresa(nombreEmpresa).getPixelcoins() - totalAPagar);
    }

    private void pagarDividendoAccionAJugador (Jugador jugador, int cantidad, double dividendoPorAccion, String nombreEmpresa) {
        double dividendo = cantidad * dividendoPorAccion;

        jugadoresMySQL.setEstadisticas(jugador.getNombre(), jugador.getPixelcoins() + dividendo, jugador.getNventas(), jugador.getIngresos() + dividendo, jugador.getGastos());

        Pixelcoin.publish(new EmpresaServerDividendoPagadoEvento(jugador.getNombre(), nombreEmpresa, dividendo));
    }
}
