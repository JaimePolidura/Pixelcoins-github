package es.serversurvival.bolsa.other.venderofertameracadoaserver;

import es.serversurvival.bolsa.posicionesabiertas._shared.newformat.domain.PosicionAbierta;
import es.serversurvival.bolsa.other._shared.ofertasmercadoserver.mysql.TipoOfertante;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;


public final class VenderOfertaAccionServerUseCase implements AllMySQLTablesInstances {
    public static final VenderOfertaAccionServerUseCase INSTANCE = new VenderOfertaAccionServerUseCase();

    private VenderOfertaAccionServerUseCase() {}

    public void vender(String playerName, PosicionAbierta posicionAVender, double precio) {
        ofertasMercadoServerMySQL.nueva(playerName, posicionAVender.getNombreActivo(), precio, posicionAVender.getCantidad(), TipoOfertante.JUGADOR, posicionAVender.getPrecioApertura());
        posicionesAbiertasMySQL.borrarPosicionAbierta(posicionAVender.getId());
    }
}
