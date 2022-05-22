package es.serversurvival.bolsa.activosinfo.eventlisteners;

import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionCerradaEvento;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class OnPosicionCerrada {
    private final ActivosInfoService activoInfoService;
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;

    public OnPosicionCerrada() {
        this.activoInfoService = DependecyContainer.get(ActivosInfoService.class);
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
    }

    @EventListener(pritority = Priority.LOWEST)
    public void on (PosicionCerradaEvento e) {
        String nombreActivo = e.getTicker();

        if(!this.posicionesAbiertasSerivce.existsByNombreActivo(nombreActivo)){
            this.activoInfoService.deleteByNombreActivo(nombreActivo);
        }
    }
}
