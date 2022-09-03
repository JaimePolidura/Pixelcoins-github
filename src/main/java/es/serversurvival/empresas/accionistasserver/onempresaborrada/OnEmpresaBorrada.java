package es.serversurvival.empresas.accionistasserver.onempresaborrada;

import es.dependencyinjector.annotations.Component;
import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.empresas.empresas.borrar.EmpresaBorrada;

@Component
public final class OnEmpresaBorrada {
    private final AccionistasServerService accionistasServerService;

    public OnEmpresaBorrada() {
        this.accionistasServerService = DependecyContainer.get(AccionistasServerService.class);
    }

    @EventListener(pritority = Priority.LOWEST)
    public void on(EmpresaBorrada empresaBorradaEvento){
        this.accionistasServerService.findByEmpresa(empresaBorradaEvento.getEmpresa()).forEach(accionista -> {
            this.accionistasServerService.deleteById(accionista.getAccionistaServerId());
        });
    }
}
