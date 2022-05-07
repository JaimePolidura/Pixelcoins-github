package es.serversurvival.bolsa.comprarofertasmercadoserver;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.bolsa._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa._shared.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.bolsa._shared.ofertasmercadoserver.mysql.TipoOfertante;
import es.serversurvival.bolsa._shared.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.jugadores._shared.newformat.application.JugadoresService;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;


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

        Jugador comprador = jugadoresMySQL.getJugador(compradorName);

        jugadoresService.save(comprador.decrementPixelcoinsBy(oferta.getPrecio()));

        if(oferta.getTipo_ofertante() == TipoOfertante.JUGADOR){
            Jugador vendedor = jugadoresMySQL.getJugador(oferta.getJugador());


            double beneficiosPerdidas = (oferta.getPrecio() - oferta.getPrecio_apertura()) * cantidadAComprar;
            double rentabilidad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(oferta.getPrecio_apertura(), oferta.getPrecio()), 3);

            if(beneficiosPerdidas >= 0)
                jugadoresMySQL.setEstadisticas(vendedor.getNombre(), vendedor.getPixelcoins() + precioTotalAPagar, vendedor.getNVentas(),
                        vendedor.getIngresos(), vendedor.getGastos() + beneficiosPerdidas);
            else
                jugadoresMySQL.setEstadisticas(vendedor.getNombre(), vendedor.getPixelcoins() + precioTotalAPagar, vendedor.getNVentas(),
                        vendedor.getIngresos() + beneficiosPerdidas, vendedor.getGastos());
        }

        Pixelcoin.publish(new EmpresaServerAccionCompradaEvento(compradorName, precioTotalAPagar, cantidadAComprar, oferta, oferta.getEmpresa()));
    }
}
