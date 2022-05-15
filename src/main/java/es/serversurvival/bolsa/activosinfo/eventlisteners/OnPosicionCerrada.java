package es.serversurvival.bolsa.activosinfo.eventlisteners;

import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionCerradaEvento;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivoInfoService;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;

public final class OnPosicionCerrada {
    private final ActivoInfoService activoInfoService;
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;

    public OnPosicionCerrada() {
        this.activoInfoService = DependecyContainer.get(ActivoInfoService.class);
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
    }

    @EventListener(pritority = Priority.LOWEST)
    public void on (PosicionCerradaEvento e) {
        String nombreActivo = e.getNombreAcitvo();

        if(!this.posicionesAbiertasSerivce.existsNombreActivo(nombreActivo)){
            this.activoInfoService.deleteByNombreActivo(nombreActivo);
        }
    }
}
