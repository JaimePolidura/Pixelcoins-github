package es.serversurvival.jugadores._shared.application;

import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.eventospixelcoins.PluginIniciado;

public final class OnInitJugadores {
    private final JugadoresService jugadoresService;

    public OnInitJugadores() {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    @EventListener(pritority = Priority.HIGHEST)
    public void on(PluginIniciado e){
        this.jugadoresService.initalizeCache();
    }
}
