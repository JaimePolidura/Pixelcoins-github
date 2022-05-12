package es.serversurvival.bolsa.other.venderofertameracadoaserver;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.bolsa.other._shared.ofertasmercadoserver.mysql.TipoOfertante;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;


public final class VenderOfertaAccionServerUseCase implements AllMySQLTablesInstances {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;

    public VenderOfertaAccionServerUseCase() {
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
    }

    public void vender(String playerName, PosicionAbierta posicionAVender, double precio) {
        ofertasMercadoServerMySQL.nueva(playerName, posicionAVender.getNombreActivo(), precio, posicionAVender.getCantidad(), TipoOfertante.JUGADOR, posicionAVender.getPrecioApertura());
        posicionesAbiertasSerivce.deleteById(posicionAVender.getPosicionAbiertaId());
    }
}
