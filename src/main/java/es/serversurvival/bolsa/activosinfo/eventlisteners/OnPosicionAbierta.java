package es.serversurvival.bolsa.activosinfo.eventlisteners;

import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.eventospixelcoins.PosicionAbiertaEvento;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivoInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;

public final class OnPosicionAbierta implements AllMySQLTablesInstances {
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
