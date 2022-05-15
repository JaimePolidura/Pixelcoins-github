package es.serversurvival.bolsa.activosinfo.eventlisteners;

import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbiertaEvento;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivoInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;

public final class OnPosicionAbierta {
    private final ActivoInfoService activoInfoService;

    public OnPosicionAbierta() {
        this.activoInfoService = DependecyContainer.get(ActivoInfoService.class);
    }

    @EventListener(pritority = Priority.LOWEST)
    public void onOpenedPosition (PosicionAbiertaEvento e) {
        String nombreActivo = e.getNombreActivo();

        if(!this.activoInfoService.existsByNombreActivo(nombreActivo)){
            String nombreActivoLargo = e.getTipoActivo().getTipoActivoService().getNombreActivoLargo(nombreActivo);

            this.activoInfoService.save(new ActivoInfo(
                    nombreActivo, e.getPrecioUnidad(), e.getTipoActivo(), nombreActivoLargo
            ));
        }

    }
}
