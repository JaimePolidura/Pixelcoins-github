package es.serversurvival.bolsa.activosinfo.eventlisteners;

import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbiertaEvento;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public final class OnPosicionAbierta {
    private final ActivosInfoService activoInfoService;

    public OnPosicionAbierta() {
        this.activoInfoService = DependecyContainer.get(ActivosInfoService.class);
    }

    @SneakyThrows
    @EventListener(pritority = Priority.LOWEST)
    public void onOpenedPosition (PosicionAbiertaEvento e) {
        if(!this.activoInfoService.existsByNombreActivo(e.getTicker())){
            String nombreActivoLargo = e.getNombreActivo() == null ?
                    e.getTipoActivo().getTipoActivoService().getNombreActivoLargo(e.getTicker()) :
                    e.getNombreActivo();

            this.activoInfoService.save(new ActivoInfo(
                    e.getTicker(), e.getPrecioUnidad(), e.getTipoActivo(), nombreActivoLargo
            ));
        }

    }
}
