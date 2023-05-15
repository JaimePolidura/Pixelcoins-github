package es.serversurvival.v1.empresas.ofertasaccionesserver.onempresaborrada;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival.v1.empresas.empresas.borrar.EmpresaBorrada;
import es.serversurvival.v1.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import lombok.RequiredArgsConstructor;

@EventHandler
@RequiredArgsConstructor
public final class OnEmpresaBorrada {
    private final OfertasAccionesServerService ofertasAccionesServerService;

    @EventListener(pritority = Priority.LOWEST)
    public void on(EmpresaBorrada event){
        this.ofertasAccionesServerService.findByEmpresa(event.getEmpresa()).forEach(oferta -> {
            this.ofertasAccionesServerService.deleteById(oferta.getOfertaAccionServerId());
        });
    }

}
