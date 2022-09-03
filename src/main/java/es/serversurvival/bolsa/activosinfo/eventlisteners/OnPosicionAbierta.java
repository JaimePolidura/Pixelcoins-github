package es.serversurvival.bolsa.activosinfo.eventlisteners;

import es.dependencyinjector.annotations.Component;
import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbiertaEvento;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
@Component
public final class OnPosicionAbierta {
    private final ActivosInfoService activoInfoService;

    @SneakyThrows
    @EventListener(pritority = Priority.LOWEST)
    public void onOpenedPosition (PosicionAbiertaEvento e) {
        if(!this.activoInfoService.existsByNombreActivo(e.getNombreActivo())){
            String nombreActivoLargo = e.getNombreActivoLargo() == null ?
                    e.getTipoActivo().getTipoActivoService().getNombreActivoLargo(e.getNombreActivo()) :
                    e.getNombreActivoLargo();

            this.activoInfoService.save(new ActivoInfo(
                    e.getNombreActivo(), e.getPrecioUnidad(), e.getTipoActivo(), nombreActivoLargo
            ));
        }

    }
}
