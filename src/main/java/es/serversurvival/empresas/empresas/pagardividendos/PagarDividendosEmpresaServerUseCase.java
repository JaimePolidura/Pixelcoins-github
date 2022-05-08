package es.serversurvival.empresas.empresas.pagardividendos;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa._shared.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.bolsa._shared.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.jugadores._shared.newformat.application.JugadoresService;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

import java.util.List;
import java.util.Map;

public final class PagarDividendosEmpresaServerUseCase implements AllMySQLTablesInstances {
    private final EmpresasService empresasService;
    private final JugadoresService jugadoresService;

    public PagarDividendosEmpresaServerUseCase(){
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    public void pagarDividendoAccionServer(String nombreEmpresa, double dividendoPorAccion, double totalAPagar) {
        var empresa = this.empresasService.getEmpresaByNombre(nombreEmpresa);
        List<PosicionAbierta> posicionesAccion = posicionesAbiertasMySQL.getPosicionesAccionesServer(nombreEmpresa);
        List<OfertaMercadoServer> ofertasAccion =  ofertasMercadoServerMySQL.getOfertasEmpresa(nombreEmpresa, OfertaMercadoServer::esTipoOfertanteJugador);

        Map<String, Jugador> allJugadoresMap = jugadoresMySQL.getMapAllJugadores();

        posicionesAccion.forEach(posicion -> {
            pagarDividendoAccionAJugador(allJugadoresMap.get(posicion.getJugador()), posicion.getCantidad(), dividendoPorAccion, nombreEmpresa);
        });
        ofertasAccion.forEach(oferta -> {
            pagarDividendoAccionAJugador(allJugadoresMap.get(oferta.getJugador()), oferta.getCantidad(), dividendoPorAccion, nombreEmpresa);
        });

        this.empresasService.save(empresa.decrementPixelcoinsBy(totalAPagar));
    }

    private void pagarDividendoAccionAJugador (Jugador jugador, int cantidad, double dividendoPorAccion, String nombreEmpresa) {
        double dividendoTotal = cantidad * dividendoPorAccion;
        this.jugadoresService.save(jugador.incrementPixelcoinsBy(dividendoTotal)
                .incrementIngresosBy(dividendoTotal));

        Pixelcoin.publish(new EmpresaServerDividendoPagadoEvento(jugador.getNombre(), nombreEmpresa, dividendoTotal));
    }
}
