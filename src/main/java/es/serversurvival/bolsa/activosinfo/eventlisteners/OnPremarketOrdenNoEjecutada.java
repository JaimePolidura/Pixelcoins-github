package es.serversurvival.bolsa.activosinfo.eventlisteners;

import es.dependencyinjector.dependencies.annotations.Component;
import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.ordenespremarket.ejecutarordenes.OrdenNoEjecutadoEvento;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public final class OnPremarketOrdenNoEjecutada {
    private final ActivosInfoService activoInfoService;
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;

    @EventListener(pritority = Priority.LOWEST)
    public void onOrdenNoEjecutada (OrdenNoEjecutadoEvento evento) {
        String nombreActivo = evento.getNombreActivo();

        if(!this.posicionesAbiertasSerivce.existsByNombreActivo(nombreActivo))
            this.activoInfoService.deleteByNombreActivo(nombreActivo);
    }
}
