package es.serversurvival.bolsa.activosinfo.eventlisteners;

import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbiertaEvento;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import lombok.SneakyThrows;

public final class OnPosicionAbierta {
    private final ActivosInfoService activoInfoService;

    public OnPosicionAbierta() {
        this.activoInfoService = DependecyContainer.get(ActivosInfoService.class);
    }

    @SneakyThrows
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
