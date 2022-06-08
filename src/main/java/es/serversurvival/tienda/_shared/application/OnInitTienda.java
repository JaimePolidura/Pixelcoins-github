package es.serversurvival.tienda._shared.application;

import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.eventospixelcoins.PluginIniciado;

public final class OnInitTienda {
    private final TiendaService tiendaService;

    public OnInitTienda() {
        this.tiendaService = DependecyContainer.get(TiendaService.class);
    }

    @EventListener(pritority = Priority.HIGHEST)
    public void onInit(PluginIniciado e){
        this.tiendaService.initializeCache();
    }

}

