package es.serversurvival.empresas.ofertasaccionesserver.venderofertameracadoaserver;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.TipoAccionista;


public final class VenderOfertaAccionServerUseCase {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final OfertasAccionesServerService ofertasAccionesServerService;

    public VenderOfertaAccionServerUseCase() {
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.ofertasAccionesServerService = DependecyContainer.get(OfertasAccionesServerService.class);
    }

    public void vender(String playerName, PosicionAbierta posicionAVender, double precio) {
        ofertasAccionesServerService.save(playerName, posicionAVender.getNombreActivo(), precio,
                posicionAVender.getCantidad(), TipoAccionista.JUGADOR, posicionAVender.getPrecioApertura());
        posicionesAbiertasSerivce.deleteById(posicionAVender.getPosicionAbiertaId());
    }
}
