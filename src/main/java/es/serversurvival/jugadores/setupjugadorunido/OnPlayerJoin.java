package es.serversurvival.jugadores.setupjugadorunido;

import es.jaimetruman.annotations.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@Component
public final class OnPlayerJoin implements Listener {
    private final SetUpJugadorUseCase useCase;

    public OnPlayerJoin(){
        this.useCase = new SetUpJugadorUseCase();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evento) {
        useCase.setUpJugadorUnido(evento.getPlayer());
    }
}
