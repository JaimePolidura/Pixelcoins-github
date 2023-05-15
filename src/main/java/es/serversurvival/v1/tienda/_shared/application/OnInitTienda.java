package es.serversurvival.v1.tienda._shared.application;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival.v1._shared.eventospixelcoins.PluginIniciado;
import lombok.AllArgsConstructor;

@EventHandler
@AllArgsConstructor
public final class OnInitTienda {
    private final TiendaService tiendaService;

    @EventListener(pritority = Priority.HIGHEST)
    public void onInit(PluginIniciado e){
        this.tiendaService.initializeCache();
    }
}

