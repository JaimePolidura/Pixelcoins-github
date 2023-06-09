package es.serversurvival.minecraftserver.scoreboards.listeners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival.minecraftserver.scoreboards.ScoreboardDisplayer;
import es.serversurvival.pixelcoins._shared.EventoTipoTransaccion;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@EventHandler
@RequiredArgsConstructor
public final class OnTransactionScoreboard {
    private final ScoreboardDisplayer scoreboardDisplayer;

    @EventListener(value = {EventoTipoTransaccion.class}, pritority = Priority.LOWEST)
    public void on(EventoTipoTransaccion transaccion) {
        Player player;

        if((player = Bukkit.getPlayer(transaccion.pagadoId())) != null ||
                (player = Bukkit.getPlayer(transaccion.pagadorId())) != null){
            scoreboardDisplayer.showActualScoreboard(player);
        }
    }
}
