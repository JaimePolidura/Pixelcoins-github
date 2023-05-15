package es.serversurvival.v1.bolsa.activosinfo.eventlisteners;

import es.dependencyinjector.dependencies.annotations.Component;
import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival.v1.bolsa.activosinfo._shared.application.ActivoInfoDataService;
import es.serversurvival.v1.bolsa.posicionesabiertas._shared.domain.PosicionAbiertaEvento;
import es.serversurvival.v1.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.ActivoInfo;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
@Component
public final class OnPosicionAbierta {
    private final ActivoInfoDataService activoInfoDataService;
    private final ActivosInfoService activoInfoService;

    @SneakyThrows
    @EventListener(pritority = Priority.LOWEST)
    public void onOpenedPosition (PosicionAbiertaEvento e) {
        if(!this.activoInfoService.existsByNombreActivo(e.getNombreActivo())){
            String nombreActivoLargo = activoInfoDataService.getNombreActivoLargo(e.getTipoActivo(), e.getNombreActivo());

            this.activoInfoService.save(new ActivoInfo(
                    e.getNombreActivo(), e.getPrecioUnidad(), e.getTipoActivo(), nombreActivoLargo
            ));
        }

    }
}
