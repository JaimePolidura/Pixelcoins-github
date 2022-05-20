package es.serversurvival.empresas.accionistasempresasserver.onempresaborrada;

import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.accionistasempresasserver._shared.application.AccionistasEmpresasServerService;
import es.serversurvival.empresas.empresas.borrar.EmpresaBorrada;

public final class OnEmpresaBorrada {
    private final AccionistasEmpresasServerService accionistasEmpresasServerService;

    public OnEmpresaBorrada() {
        this.accionistasEmpresasServerService = DependecyContainer.get(AccionistasEmpresasServerService.class);
    }

    @EventListener(pritority = Priority.LOWEST)
    public void on(EmpresaBorrada empresaBorradaEvento){
        this.accionistasEmpresasServerService.findByEmpresa(empresaBorradaEvento.getEmpresa()).forEach(accionista -> {
            this.accionistasEmpresasServerService.deleteById(accionista.getAccionEmpresaServerId());
        });
    }
}
