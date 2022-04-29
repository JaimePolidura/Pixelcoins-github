package es.serversurvival.bolsa.cancelarofertamercadoserver;

import es.serversurvival.bolsa._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa._shared.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.bolsa._shared.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class CancelarOfertaAccionServerUseCase implements AllMySQLTablesInstances {
    public static final CancelarOfertaAccionServerUseCase INSTANCE = new CancelarOfertaAccionServerUseCase();

    private CancelarOfertaAccionServerUseCase() {}

    public void cancelar(String player, int id) {
        OfertaMercadoServer oferta = ofertasMercadoServerMySQL.get(id);

        ofertasMercadoServerMySQL.borrar(id);
        posicionesAbiertasMySQL.nuevaPosicion(player, TipoActivo.ACCIONES_SERVER, oferta.getEmpresa(), oferta.getCantidad(),
                oferta.getPrecio_apertura(), TipoPosicion.LARGO);
    }


}
