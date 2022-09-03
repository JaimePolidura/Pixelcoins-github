package es.serversurvival.empresas.ofertasaccionesserver.onempresaborrada;

import es.jaime.EventListener;
import es.jaime.Priority;
import es.dependencyinjector.annotations.Component;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empresas.borrar.EmpresaBorrada;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;

@Component
public final class OnEmpresaBorrada {
    private final OfertasAccionesServerService ofertasAccionesServerService;

    public OnEmpresaBorrada() {
        this.ofertasAccionesServerService = DependecyContainer.get(OfertasAccionesServerService.class);
    }

    @EventListener(pritority = Priority.LOWEST)
    public void on(EmpresaBorrada event){
        this.ofertasAccionesServerService.findByEmpresa(event.getEmpresa()).forEach(oferta -> {
            this.ofertasAccionesServerService.deleteById(oferta.getOfertaAccionServerId());
        });
    }

}
