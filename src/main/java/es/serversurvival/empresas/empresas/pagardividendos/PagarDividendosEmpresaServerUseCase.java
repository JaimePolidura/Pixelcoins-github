package es.serversurvival.empresas.empresas.pagardividendos;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival.Pixelcoin;

import java.util.List;
import java.util.Map;

public final class PagarDividendosEmpresaServerUseCase {
    private final EmpresasService empresasService;
    private final JugadoresService jugadoresService;
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final OfertasAccionesServerService ofertasAccionesServerService;

    public PagarDividendosEmpresaServerUseCase(){
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.ofertasAccionesServerService = DependecyContainer.get(OfertasAccionesServerService.class);
    }

    public void pagarDividendoAccionServer(String nombreEmpresa, double dividendoPorAccion, double totalAPagar) {
        var empresa = this.empresasService.getEmpresaByNombre(nombreEmpresa);
        //TODO
        List<PosicionAbierta> posicionesAccionDeEmpresa = posicionesAbiertasSerivce.findAll().stream()
                .filter(posicionAbierta -> posicionAbierta.getNombreActivo().equalsIgnoreCase(nombreEmpresa))
                .toList();
        List<OfertaAccionServer> ofertasAccion =  ofertasAccionesServerService.findByEmpresa(nombreEmpresa, OfertaAccionServer::esTipoOfertanteJugador);

        Map<String, Jugador> allJugadoresMap = jugadoresService.getMapAllJugadores();

        posicionesAccionDeEmpresa.forEach(posicion -> {
            pagarDividendoAccionAJugador(allJugadoresMap.get(posicion.getJugador()), posicion.getCantidad(), dividendoPorAccion, nombreEmpresa);
        });
        ofertasAccion.forEach(oferta -> {
            pagarDividendoAccionAJugador(allJugadoresMap.get(oferta.getNombreOfertante()), oferta.getCantidad(), dividendoPorAccion, nombreEmpresa);
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
