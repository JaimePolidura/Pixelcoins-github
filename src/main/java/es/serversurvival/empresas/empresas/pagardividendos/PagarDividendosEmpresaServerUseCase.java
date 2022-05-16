package es.serversurvival.empresas.empresas.pagardividendos;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.accionistasempresasserver._shared.application.AccionistasEmpresasServerService;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.AccionEmpresaServer;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.empresas.empresas.pagardividendos.eventos.EmpresaServerDividendoPagadoEmpresa;
import es.serversurvival.empresas.empresas.pagardividendos.eventos.EmpresaServerDividendoPagadoJugador;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.Pixelcoin;

public final class PagarDividendosEmpresaServerUseCase {
    private final EmpresasService empresasService;
    private final JugadoresService jugadoresService;
    private final AccionistasEmpresasServerService accionistasEmpresasServerService;

    public PagarDividendosEmpresaServerUseCase(){
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.accionistasEmpresasServerService = DependecyContainer.get(AccionistasEmpresasServerService.class);
    }

    public void pagarDividendoAccionServer(String nombreEmpresa, double dividendoPorAccion) {
        var empresa = this.empresasService.getByNombre(nombreEmpresa);

        this.accionistasEmpresasServerService.findByEmpresa(nombreEmpresa, AccionEmpresaServer::esJugador).forEach(accionista -> {
            pagarDividendoAccionAJugador(accionista, dividendoPorAccion, nombreEmpresa);
        });
        this.accionistasEmpresasServerService.findByEmpresa(nombreEmpresa, AccionEmpresaServer::esEmpresa).forEach(accionista -> {
            pagarDividendoAEmpresa(dividendoPorAccion, empresa, accionista);
        });

        this.empresasService.save(empresa.decrementPixelcoinsBy(dividendoPorAccion * empresa.getAccionesTotales()));
    }

    private void pagarDividendoAEmpresa(double dividendoPorAccion, Empresa empresa, AccionEmpresaServer accionista) {
        double totalACobrar = accionista.getCantidad() * dividendoPorAccion;

        this.empresasService.save(empresa.incrementPixelcoinsBy(totalACobrar));

        Pixelcoin.publish(new EmpresaServerDividendoPagadoEmpresa(empresa.getNombre(), dividendoPorAccion * accionista.getCantidad()));
    }

    private void pagarDividendoAccionAJugador (AccionEmpresaServer accionista, double dividendoPorAccion, String nombreEmpresa) {
        double dividendoTotal = accionista.getCantidad() * dividendoPorAccion;
        var jugador = this.jugadoresService.getByNombre(accionista.getNombreAccionista());
        this.jugadoresService.save(jugador.incrementPixelcoinsBy(dividendoTotal)
                .incrementIngresosBy(dividendoTotal));

        Pixelcoin.publish(new EmpresaServerDividendoPagadoJugador(jugador.getNombre(), nombreEmpresa, dividendoTotal));
    }
}
