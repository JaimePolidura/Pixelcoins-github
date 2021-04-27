package es.serversurvival.nfs.bolsa.ofertasmercadoserver.comprar;

import es.serversurvival.legacy.mySQL.eventos.empresas.EmpresaServerAccionCompradaEvento;
import es.serversurvival.nfs.Pixelcoin;
import es.serversurvival.nfs.bolsa.llamadasapi.mysql.TipoActivo;
import es.serversurvival.nfs.bolsa.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.nfs.bolsa.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;


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
