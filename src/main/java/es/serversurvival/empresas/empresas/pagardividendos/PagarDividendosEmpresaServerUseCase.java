package es.serversurvival.empresas.empresas.pagardividendos;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.IllegalState;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.empresas.accionistasempresasserver._shared.application.AccionistasServerService;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.AccionistaServer;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.empresas.empresas.pagardividendos.eventos.EmpresaServerDividendoPagadoEmpresa;
import es.serversurvival.empresas.empresas.pagardividendos.eventos.EmpresaServerDividendoPagadoJugador;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class PagarDividendosEmpresaServerUseCase {
    private final EmpresasService empresasService;
    private final JugadoresService jugadoresService;
    private final AccionistasServerService accionistasEmpresasServerService;
    private final EventBus eventBus;

    public PagarDividendosEmpresaServerUseCase(){
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.accionistasEmpresasServerService = DependecyContainer.get(AccionistasServerService.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }

    public void pagar(String jugadorNombre, String nombreEmpresa, double dividendoPorAccion) {
        this.ensureDividendoCorrectFormat(dividendoPorAccion);
        var empresa = this.empresasService.getByNombre(nombreEmpresa);
        this.ensureOwnerOfEmpresa(jugadorNombre, empresa);
        this.ensureCotizada(empresa);
        this.ensureEnoughPixelcions(empresa, dividendoPorAccion);

        this.accionistasEmpresasServerService.findByEmpresa(nombreEmpresa).forEach(accionista -> {
            if(accionista.esEmpresa())
                pagarDividendoAEmpresa(dividendoPorAccion, empresa, accionista);
            else
                pagarDividendoAccionAJugador(accionista, dividendoPorAccion, nombreEmpresa);
        });

        this.empresasService.save(empresa.decrementPixelcoinsBy(dividendoPorAccion * empresa.getAccionesTotales()));
    }

    private void ensureEnoughPixelcions(Empresa empresa, double dividendoPorAccion) {
        if(empresa.getPixelcoins() < (dividendoPorAccion * empresa.getAccionesTotales()))
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcions para repartir dividendos");
    }

    private void ensureCotizada(Empresa empresa) {
        if(!empresa.isCotizada())
            throw new IllegalState("La empresa tiene que cotizar en bolsa para que puedes repartir dividendos");
    }

    private void pagarDividendoAEmpresa(double dividendoPorAccion, Empresa empresa, AccionistaServer accionista) {
        double totalACobrar = accionista.getCantidad() * dividendoPorAccion;

        this.empresasService.save(empresa.incrementPixelcoinsBy(totalACobrar));

        this.eventBus.publish(new EmpresaServerDividendoPagadoEmpresa(empresa.getNombre(), dividendoPorAccion * accionista.getCantidad()));
    }

    private void pagarDividendoAccionAJugador (AccionistaServer accionista, double dividendoPorAccion, String nombreEmpresa) {
        double dividendoTotal = accionista.getCantidad() * dividendoPorAccion;
        var jugador = this.jugadoresService.getByNombre(accionista.getNombreAccionista());
        this.jugadoresService.save(jugador.incrementPixelcoinsBy(dividendoTotal)
                .incrementIngresosBy(dividendoTotal));

        this.eventBus.publish(new EmpresaServerDividendoPagadoJugador(jugador.getNombre(), nombreEmpresa, dividendoTotal));
    }

    private void ensureDividendoCorrectFormat(double dividendoPorAccion){
        if(dividendoPorAccion <= 0)
            throw new IllegalQuantity("El dividendo tiene que ser mayor que cero");
    }

    private void ensureOwnerOfEmpresa(String jugadorNombre, Empresa empresa){
        if(!empresa.getOwner().equalsIgnoreCase(jugadorNombre))
            throw new NotTheOwner("No eres el owner de la empresa");
    }
}
