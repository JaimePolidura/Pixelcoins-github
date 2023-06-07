package es.serversurvival.pixelcoins.jugadores.setupnuevojugador;

import es.dependencyinjector.dependencies.annotations.Component;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@Component
@RequiredArgsConstructor
public final class OnPlayerJoin implements Listener {
    private final SetUpJugadorUseCase useCase;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evento) {
        useCase.setUpJugadorUnido(evento.getPlayer());
    }
}
