package es.serversurvival.empresas.ofertasaccionesserver.cancelarofertamercadoserver;

import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.empresas.accionistasempresasserver._shared.application.AccionistasEmpresasServerService;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;

import java.util.UUID;

public final class CancelarOfertaAccionServerUseCase {
    private final OfertasAccionesServerService ofertasAccionesServerService;

    public CancelarOfertaAccionServerUseCase() {
        this.ofertasAccionesServerService = DependecyContainer.get(OfertasAccionesServerService.class);
    }

    public void cancelar(String player, UUID id) {
        var oferta = ofertasAccionesServerService.getById(id);
        this.ensureOwnsOferta(player, oferta);

        this.ofertasAccionesServerService.deleteById(oferta.getOfertasAccionesServerId());
    }

    private void ensureOwnsOferta(String playerName, OfertaAccionServer oferta){
        if(!oferta.getNombreOfertante().equalsIgnoreCase(playerName))
            throw new NotTheOwner("La oferta no te pertenece");
    }


}
