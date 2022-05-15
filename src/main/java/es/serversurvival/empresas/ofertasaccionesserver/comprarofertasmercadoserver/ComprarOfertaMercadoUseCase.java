package es.serversurvival.empresas.ofertasaccionesserver.comprarofertasmercadoserver;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.jugadores._shared.application.JugadoresService;

import java.util.UUID;


public final class ComprarOfertaMercadoUseCase {
    private final JugadoresService jugadoresService;
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;

    public ComprarOfertaMercadoUseCase () {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
    }

    public void comprarOfertaMercadoAccionServer (String compradorName, UUID idOfeta, int cantidadAComprar) {
//        OfertaMercadoServer oferta = ofertasMercadoServerMySQL.get(idOfeta);
//        double precioTotalAPagar = oferta.getPrecio() * cantidadAComprar;
//
//        posicionesAbiertasSerivce.save(compradorName, TipoActivo.ACCIONES_SERVER, oferta.getEmpresa(), cantidadAComprar, oferta.getPrecio(), TipoPosicion.LARGO);
//        ofertasMercadoServerMySQL.setCantidadOBorrar(idOfeta, oferta.getCantidad() - cantidadAComprar);
//
//        Jugador comprador = jugadoresService.getByNombre(compradorName);
//
//        jugadoresService.save(comprador.decrementPixelcoinsBy(oferta.getPrecio()));
//
//        if(oferta.getTipo_ofertante() == TipoOfertante.JUGADOR){
//            Jugador vendedor = jugadoresService.getByNombre(oferta.getJugador());
//
//            double beneficiosPerdidas = (oferta.getPrecio() - oferta.getPrecio_apertura()) * cantidadAComprar;
//
//            if(beneficiosPerdidas >= 0)
//                this.jugadoresService.save(vendedor.incrementPixelcoinsBy(precioTotalAPagar).incrementGastosBy(beneficiosPerdidas));
//            else
//                this.jugadoresService.save(vendedor.incrementPixelcoinsBy(precioTotalAPagar).incrementIngresosBy(beneficiosPerdidas));
//        }
//
//        Pixelcoin.publish(new EmpresaServerAccionCompradaEvento(compradorName, precioTotalAPagar, cantidadAComprar, oferta, oferta.getEmpresa()));
    }
}
