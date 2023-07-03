package es.serversurvival.minecraftserver.scoreboards.listeners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival.minecraftserver.scoreboards.ScoreboardDisplayer;
import es.serversurvival.pixelcoins._shared.EventoTipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.domain.Transaccion;
import es.serversurvival.pixelcoins.transacciones.domain.TransaccionCreada;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@EventHandler
@RequiredArgsConstructor
public final class OnTransactionScoreboard {
    private final ScoreboardDisplayer scoreboardDisplayer;

    @EventListener(pritority = Priority.LOWEST)
    public void on(TransaccionCreada transaccionCreada) {
        Player player;
        Transaccion transaccion = transaccionCreada.getTransaccion();

        if((player = Bukkit.getPlayer(transaccion.getPagadoId())) != null ||
                (player = Bukkit.getPlayer(transaccion.getPagadorId())) != null){
            scoreboardDisplayer.showActualScoreboard(player);
        }
    }
}
