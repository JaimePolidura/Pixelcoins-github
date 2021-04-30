package es.serversurvival.bolsa.ofertasmercadoserver.cancelar;

import es.serversurvival.bolsa.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.bolsa.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class CancelarOfertaAccionServerUseCase implements AllMySQLTablesInstances {
    public static final CancelarOfertaAccionServerUseCase INSTANCE = new CancelarOfertaAccionServerUseCase();

    private CancelarOfertaAccionServerUseCase() {}

    public void cancelar(String player, int id) {
        OfertaMercadoServer oferta = ofertasMercadoServerMySQL.get(id);
        ofertasMercadoServerMySQL.borrar(id);
        posicionesAbiertasMySQL.nuevaPosicion(player, TipoActivo.ACCIONES_SERVER, oferta.getEmpresa(), oferta.getCantidad(), oferta.getPrecio_apertura(), TipoPosicion.LARGO);
    }


}
