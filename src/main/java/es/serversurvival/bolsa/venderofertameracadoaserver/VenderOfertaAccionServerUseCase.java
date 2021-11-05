package es.serversurvival.bolsa.venderofertameracadoaserver;

import es.serversurvival.bolsa._shared.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.bolsa._shared.ofertasmercadoserver.mysql.TipoOfertante;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;


public final class VenderOfertaAccionServerUseCase implements AllMySQLTablesInstances {
    public static final VenderOfertaAccionServerUseCase INSTANCE = new VenderOfertaAccionServerUseCase();

    private VenderOfertaAccionServerUseCase() {}

    public void vender(String playerName, PosicionAbierta posicionAVender, double precio) {
        ofertasMercadoServerMySQL.nueva(playerName, posicionAVender.getNombre_activo(), precio, posicionAVender.getCantidad(), TipoOfertante.JUGADOR, posicionAVender.getPrecio_apertura());
        posicionesAbiertasMySQL.borrarPosicionAbierta(posicionAVender.getId());
    }
}
