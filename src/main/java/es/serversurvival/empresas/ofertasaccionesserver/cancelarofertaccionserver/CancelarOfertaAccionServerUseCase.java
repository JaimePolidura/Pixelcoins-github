package es.serversurvival.empresas.ofertasaccionesserver.cancelarofertaccionserver;

import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaimetruman.annotations.Repository;
import es.jaimetruman.annotations.UseCase;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@UseCase
public final class CancelarOfertaAccionServerUseCase {
    private final OfertasAccionesServerService ofertasAccionesServerService;

    public CancelarOfertaAccionServerUseCase() {
        this.ofertasAccionesServerService = DependecyContainer.get(OfertasAccionesServerService.class);
    }

    public void cancelar(String player, UUID id) {
        var oferta = ofertasAccionesServerService.getById(id);
        this.ensureOwnsOferta(player, oferta);

        this.ofertasAccionesServerService.deleteById(oferta.getOfertaAccionServerId());
    }

    private void ensureOwnsOferta(String playerName, OfertaAccionServer oferta){
        if(!oferta.getNombreOfertante().equalsIgnoreCase(playerName))
            throw new NotTheOwner("La oferta no te pertenece");
    }


}
