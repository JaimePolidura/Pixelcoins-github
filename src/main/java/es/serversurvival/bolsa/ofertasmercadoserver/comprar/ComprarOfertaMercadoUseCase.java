package es.serversurvival.bolsa.ofertasmercadoserver.comprar;

import es.serversurvival.Pixelcoin;
import es.serversurvival.bolsa.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.bolsa.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;


public final class ComprarOfertaMercadoUseCase implements AllMySQLTablesInstances {
    public static final ComprarOfertaMercadoUseCase INSTANCE = new ComprarOfertaMercadoUseCase();

    private ComprarOfertaMercadoUseCase () {}

    public void comprarOfertaMercadoAccionServer (String player, int idOfeta, int cantidadAComprar) {
        OfertaMercadoServer oferta = ofertasMercadoServerMySQL.get(idOfeta);
        double precioTotalAPagar = oferta.getPrecio() * cantidadAComprar;

        posicionesAbiertasMySQL.nuevaPosicion(player, TipoActivo.ACCIONES_SERVER, oferta.getEmpresa(), cantidadAComprar, oferta.getPrecio(), TipoPosicion.LARGO);
        ofertasMercadoServerMySQL.setCantidadOBorrar(idOfeta, oferta.getCantidad() - cantidadAComprar);

        Pixelcoin.publish(new EmpresaServerAccionCompradaEvento(player, precioTotalAPagar, cantidadAComprar, oferta, oferta.getEmpresa()));
    }
}
