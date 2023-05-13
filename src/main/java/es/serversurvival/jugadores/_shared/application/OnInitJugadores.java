package es.serversurvival.jugadores._shared.application;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival._shared.eventospixelcoins.PluginIniciado;
import lombok.RequiredArgsConstructor;

@EventHandler
@RequiredArgsConstructor
public final class OnInitJugadores {
    private final JugadoresService jugadoresService;

    @EventListener(pritority = Priority.HIGHEST)
    public void on(PluginIniciado e){
        this.jugadoresService.initalizeCache();
    }
}
