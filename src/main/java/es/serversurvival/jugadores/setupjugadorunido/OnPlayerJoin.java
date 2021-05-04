package es.serversurvival.jugadores.setupjugadorunido;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class OnPlayerJoin implements Listener {
    private final SetUpJugadorUseCase useCase = SetUpJugadorUseCase.INSTANCE;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evento) {
        useCase.setUpJugadorUnido(evento.getPlayer());
    }
}
