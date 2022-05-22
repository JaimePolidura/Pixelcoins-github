package es.serversurvival.bolsa.activosinfo.eventlisteners;

import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.ordenespremarket.ejecutarordenes.OrdenNoEjecutadoEvento;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import lombok.AllArgsConstructor;
import org.checkerframework.common.util.report.qual.ReportCall;

@AllArgsConstructor
public final class OnPremarketOrdenNoEjecutada {
    private final ActivosInfoService activoInfoService;
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;

    public OnPremarketOrdenNoEjecutada() {
        this.activoInfoService = DependecyContainer.get(ActivosInfoService.class);
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
    }

    @EventListener(pritority = Priority.LOWEST)
    public void onOrdenNoEjecutada (OrdenNoEjecutadoEvento evento) {
        String nombreActivo = evento.getNombreActivo();
        
        if(!this.posicionesAbiertasSerivce.existsByNombreActivo(nombreActivo)){
            this.activoInfoService.deleteByNombreActivo(nombreActivo);
        }
    }
}
