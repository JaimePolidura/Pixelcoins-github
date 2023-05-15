package es.serversurvival.v1.empresas.ofertasaccionesserver.cancelarofertaccionserver;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.v1.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.v1.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import lombok.AllArgsConstructor;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class CancelarOfertaAccionServerUseCase {
    private final OfertasAccionesServerService ofertasAccionesServerService;

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
