package es.serversurvival.empresas.ofertasaccionesserver.cancelarofertamercadoserver;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;

import java.util.UUID;

public final class CancelarOfertaAccionServerUseCase {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;

    public CancelarOfertaAccionServerUseCase() {
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
    }

    public void cancelar(String player, UUID id) {
        //TODO
//        OfertaMercadoServer oferta = ofertasMercadoServerMySQL.get(id);
//
//        ofertasMercadoServerMySQL.borrar(id);
//        posicionesAbiertasSerivce.save(player, SupportedTipoActivo.ACCIONES, oferta.getEmpresa(), oferta.getCantidad(),
//                oferta.getPrecio_apertura(), TipoPosicion.LARGO);
    }


}
