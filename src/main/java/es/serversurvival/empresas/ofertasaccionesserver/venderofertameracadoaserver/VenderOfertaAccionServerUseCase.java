package es.serversurvival.empresas.ofertasaccionesserver.venderofertameracadoaserver;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.TipoOfertante;


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
