package es.serversurvival.empresas.ofertasaccionesserver.comprarofertasaccionesserver;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.empresas.accionistasserver._shared.domain.AccionistaServer;
import es.serversurvival.empresas.accionistasserver._shared.domain.TipoAccionista;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public final class ComprarOfertaMercadoUseCase {
    public final static ComprarOfertaMercadoUseCase INSTANCE = new ComprarOfertaMercadoUseCase();

    private final JugadoresService jugadoresService;
    private final OfertasAccionesServerService ofertasAccionesServerService;
    private final AccionistasServerService accionistasServerService;
    private final EmpresasService empresasService;
    private final EventBus eventBus;

    public ComprarOfertaMercadoUseCase () {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.ofertasAccionesServerService= DependecyContainer.get(OfertasAccionesServerService.class);
        this.accionistasServerService = DependecyContainer.get(AccionistasServerService.class);
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }

    public void comprarOfertaMercadoAccionServer (String compradorName, UUID idOfeta, int cantidadAComprar) {
        var ofertaAComprar = ofertasAccionesServerService.getById(idOfeta);
        var jugadorComprador = this.jugadoresService.getByNombre(compradorName);
        var empresa = this.empresasService.getByNombre(ofertaAComprar.getEmpresa());
        var accionistaEmpresaServer = this.accionistasServerService.getById(ofertaAComprar.getAccionistaEmpresaServerId());
        this.ensusureCorrectQuantityCantidad(cantidadAComprar, ofertaAComprar);
        this.ensureCompradorNotHisSelf(compradorName, ofertaAComprar);
        this.ensureHasPixelcoins(ofertaAComprar, jugadorComprador, cantidadAComprar);
        double precioTotalAPagar = ofertaAComprar.getPrecio() * cantidadAComprar;

        this.decreaseOfertaCantidadOrRemove(cantidadAComprar, ofertaAComprar);
        this.decreaseAccionistaServerCantidadOrRemove(cantidadAComprar, accionistaEmpresaServer);
        this.accionistasServerService.save(compradorName, TipoAccionista.JUGADOR, empresa.getNombre(),
                cantidadAComprar, ofertaAComprar.getPrecio());
        this.jugadoresService.save(jugadorComprador.decrementPixelcoinsBy(precioTotalAPagar).incrementGastosBy(precioTotalAPagar));
        if(ofertaAComprar.esTipoOfertanteJugador())
            payPixelcoinsToJugadorVendedor(ofertaAComprar, precioTotalAPagar);
        else
            this.empresasService.save(empresa.incrementPixelcoinsBy(precioTotalAPagar));

        this.eventBus.publish(EmpresaServerAccionComprada.of(compradorName, ofertaAComprar.getNombreOfertante(), ofertaAComprar.getTipoOfertante(),
                empresa.getNombre(), precioTotalAPagar, cantidadAComprar));
    }

    private void payPixelcoinsToJugadorVendedor(OfertaAccionServer ofertaAComprar, double precioTotalAPagar) {
        var vendedor = jugadoresService.getByNombre(ofertaAComprar.getNombreOfertante());

        this.jugadoresService.save(vendedor.incrementPixelcoinsBy(precioTotalAPagar).incrementIngresosBy(precioTotalAPagar));
    }

    private void decreaseAccionistaServerCantidadOrRemove(int cantidadAComprar, AccionistaServer accionsta){
        if(accionsta.getCantidad() - cantidadAComprar <= 0)
            this.accionistasServerService.deleteById(accionsta.getAccionEmpresaServerId());
        else
            this.accionistasServerService.save(accionsta.decreaseCantidad(cantidadAComprar));
    }

    private void decreaseOfertaCantidadOrRemove(int cantidadAComprar, OfertaAccionServer ofertaAComprar) {
        if(ofertaAComprar.getCantidad() - cantidadAComprar <= 0)
            this.ofertasAccionesServerService.deleteById(ofertaAComprar.getOfertaAccionServerId());
        else
            this.ofertasAccionesServerService.save(ofertaAComprar.decreaseCantidadBy(cantidadAComprar));
    }

    private void ensureHasPixelcoins(OfertaAccionServer ofertaAComprar, Jugador comprador, int cantidadAComprar){
        if((cantidadAComprar * ofertaAComprar.getPrecio()) > comprador.getPixelcoins())
            throw new NotEnoughPixelcoins("No tienes las suficieentes pixelcions para comprar");
    }

    private void ensureCompradorNotHisSelf(String compradorName, OfertaAccionServer ofertaAComprar){
        if(compradorName.equalsIgnoreCase(ofertaAComprar.getNombreOfertante())){
            throw new CannotBeYourself("No te puedes autocomprar las accioens");
        }
    }

    private void ensusureCorrectQuantityCantidad(int cantidad, OfertaAccionServer ofertaAComprar){
        if(cantidad <= 0 || cantidad > ofertaAComprar.getCantidad())
            throw new IllegalQuantity("La cantidad debe comprender entre 1 y las acciones de la oferta");
    }
}
