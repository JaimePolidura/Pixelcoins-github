package es.serversurvival.bolsa.comprarofertasmercadoserver;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa._shared.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.bolsa._shared.ofertasmercadoserver.mysql.TipoOfertante;
import es.serversurvival.bolsa._shared.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;


public final class ComprarOfertaMercadoUseCase implements AllMySQLTablesInstances {
    public static final ComprarOfertaMercadoUseCase INSTANCE = new ComprarOfertaMercadoUseCase();
    private final JugadoresService jugadoresService;

    private ComprarOfertaMercadoUseCase () {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    public void comprarOfertaMercadoAccionServer (String compradorName, int idOfeta, int cantidadAComprar) {
        OfertaMercadoServer oferta = ofertasMercadoServerMySQL.get(idOfeta);
        double precioTotalAPagar = oferta.getPrecio() * cantidadAComprar;

        posicionesAbiertasMySQL.nuevaPosicion(compradorName, TipoActivo.ACCIONES_SERVER, oferta.getEmpresa(), cantidadAComprar, oferta.getPrecio(), TipoPosicion.LARGO);
        ofertasMercadoServerMySQL.setCantidadOBorrar(idOfeta, oferta.getCantidad() - cantidadAComprar);

        Jugador comprador = jugadoresService.getByNombre(compradorName);

        jugadoresService.save(comprador.decrementPixelcoinsBy(oferta.getPrecio()));

        if(oferta.getTipo_ofertante() == TipoOfertante.JUGADOR){
            Jugador vendedor = jugadoresService.getByNombre(oferta.getJugador());

            double beneficiosPerdidas = (oferta.getPrecio() - oferta.getPrecio_apertura()) * cantidadAComprar;

            if(beneficiosPerdidas >= 0)
                this.jugadoresService.save(vendedor.incrementPixelcoinsBy(precioTotalAPagar).incrementGastosBy(beneficiosPerdidas));
            else
                this.jugadoresService.save(vendedor.incrementPixelcoinsBy(precioTotalAPagar).incrementIngresosBy(beneficiosPerdidas));
        }

        Pixelcoin.publish(new EmpresaServerAccionCompradaEvento(compradorName, precioTotalAPagar, cantidadAComprar, oferta, oferta.getEmpresa()));
    }
}
