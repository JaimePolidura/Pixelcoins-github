package es.serversurvival.empresas.ofertasaccionesserver.comprarofertasmercadoserver;

import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.empresas.accionistasempresasserver._shared.application.AccionistasEmpresasServerService;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.AccionEmpresaServer;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.TipoAccionista;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;

import java.util.UUID;


public final class ComprarOfertaMercadoUseCase {
    private final JugadoresService jugadoresService;
    private final OfertasAccionesServerService ofertasAccionesServerService;
    private final AccionistasEmpresasServerService accionistasEmpresasServerService;
    private final EmpresasService empresasService;

    public ComprarOfertaMercadoUseCase () {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.ofertasAccionesServerService= DependecyContainer.get(OfertasAccionesServerService.class);
        this.accionistasEmpresasServerService = DependecyContainer.get(AccionistasEmpresasServerService.class);
        this.empresasService = DependecyContainer.get(EmpresasService.class);
    }

    public void comprarOfertaMercadoAccionServer (String compradorName, UUID idOfeta, int cantidadAComprar) {
        var ofertaAComprar = ofertasAccionesServerService.getById(idOfeta);
        var jugadorComprador = this.jugadoresService.getByNombre(compradorName);
        var empresa = this.empresasService.getEmpresaByNombre(ofertaAComprar.getEmpresa());
        var accionistaEmpresaServer = this.accionistasEmpresasServerService.getById(ofertaAComprar.getAccionistaEmpresaServerId());
        this.ensusureCorrectQuantityCantidad(cantidadAComprar, ofertaAComprar);
        this.ensureCompradorNotHisSelf(compradorName, ofertaAComprar);
        this.ensureHasPixelcoins(ofertaAComprar, jugadorComprador, cantidadAComprar);
        double precioTotalAPagar = ofertaAComprar.getPrecio() * cantidadAComprar;

        this.decreaseOfertaCantidadOrRemove(cantidadAComprar, ofertaAComprar);
        this.decreaseAccionistaEmpresaServerCantidadOrRemove(cantidadAComprar, accionistaEmpresaServer);
        this.accionistasEmpresasServerService.save(compradorName, TipoAccionista.JUGADOR, empresa.getNombre(),
                cantidadAComprar, ofertaAComprar.getPrecio());

        this.jugadoresService.save(jugadorComprador.decrementPixelcoinsBy(precioTotalAPagar).incrementGastosBy(precioTotalAPagar));
        if(ofertaAComprar.esTipoOfertanteJugador())
            payPixelcoinsToJugadorVendedor(cantidadAComprar, ofertaAComprar, precioTotalAPagar);
        else
            this.empresasService.save(empresa.incrementPixelcoinsBy(precioTotalAPagar));

        Pixelcoin.publish(new EmpresaServerAccionCompradaEvento(compradorName, precioTotalAPagar, cantidadAComprar, ofertaAComprar, ofertaAComprar.getEmpresa()));
    }

    private void payPixelcoinsToJugadorVendedor(int cantidadAComprar, OfertaAccionServer ofertaAComprar, double precioTotalAPagar) {
        var vendedor = jugadoresService.getByNombre(ofertaAComprar.getNombreOfertante());

        double beneficiosPerdidas = (ofertaAComprar.getPrecio() - ofertaAComprar.getPrecioApertura()) * cantidadAComprar;

        if(beneficiosPerdidas >= 0)
            this.jugadoresService.save(vendedor.incrementPixelcoinsBy(precioTotalAPagar).incrementGastosBy(beneficiosPerdidas));
        else
            this.jugadoresService.save(vendedor.incrementPixelcoinsBy(precioTotalAPagar).incrementIngresosBy(beneficiosPerdidas));
    }

    private void decreaseAccionistaEmpresaServerCantidadOrRemove(int cantidadAComprar, AccionEmpresaServer accionsta){
        if(accionsta.getCantidad() - cantidadAComprar <= 0)
            this.accionistasEmpresasServerService.deleteById(accionsta.getAccionEmpresaServerId());
        else
            this.accionistasEmpresasServerService.save(accionsta.decreaseCantidad(cantidadAComprar));
    }

    private void decreaseOfertaCantidadOrRemove(int cantidadAComprar, OfertaAccionServer ofertaAComprar) {
        if(ofertaAComprar.getCantidad() - cantidadAComprar <= 0)
            this.ofertasAccionesServerService.deleteById(ofertaAComprar.getOfertasAccionesServerId());
        else
            this.ofertasAccionesServerService.save(ofertaAComprar.decreaseCantidadBy(cantidadAComprar));
    }

    private void ensureHasPixelcoins(OfertaAccionServer ofertaAComprar, Jugador comprador, int cantidadAComprar){
        if((cantidadAComprar * ofertaAComprar.getPrecio()) > comprador.getPixelcoins())
            throw new NotEnoughPixelcoins("No tienes las suficieentes pixelcions para comprar");
    }

    private void ensureCompradorNotHisSelf(String compradorName, OfertaAccionServer ofertaAComprar){
        if(compradorName.equalsIgnoreCase(ofertaAComprar.getNombreOfertante()))
            throw new CannotBeYourself("No te puedes autocomprar las accioens");
    }

    private void ensusureCorrectQuantityCantidad(int cantidad, OfertaAccionServer ofertaAComprar){
        if(cantidad <= 0 || cantidad > ofertaAComprar.getCantidad())
            throw new IllegalQuantity("La cantidad debe comprender entre 1 y las acciones de la oferta");
    }
}
