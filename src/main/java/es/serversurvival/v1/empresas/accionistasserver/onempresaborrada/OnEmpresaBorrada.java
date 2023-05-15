package es.serversurvival.v1.empresas.accionistasserver.onempresaborrada;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival.v1.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.v1.empresas.empresas.borrar.EmpresaBorrada;
import lombok.AllArgsConstructor;

@EventHandler
@AllArgsConstructor
public final class OnEmpresaBorrada {
    private final AccionistasServerService accionistasServerService;

    @EventListener(pritority = Priority.LOWEST)
    public void on(EmpresaBorrada empresaBorradaEvento){
        this.accionistasServerService.findByEmpresa(empresaBorradaEvento.getEmpresa()).forEach(accionista -> {
            this.accionistasServerService.deleteById(accionista.getAccionistaServerId());
        });
    }
}
