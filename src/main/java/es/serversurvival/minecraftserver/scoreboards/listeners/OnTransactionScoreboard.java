package es.serversurvival.minecraftserver.scoreboards.listeners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival.minecraftserver.scoreboards.ScoreboardDisplayer;
import es.serversurvival.v1.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival.v1.transacciones._shared.domain.Transaccion;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@EventHandler
@RequiredArgsConstructor
public final class OnTransactionScoreboard {
    private final ScoreboardDisplayer scoreboardDisplayer;

    @EventListener(value = {EventoTipoTransaccion.class}, pritority = Priority.LOWEST)
    public void on(EventoTipoTransaccion tipoTransaccion) {
        Transaccion transacion = tipoTransaccion.buildTransaccion();
        Player player;

        if((player = Bukkit.getPlayer(transacion.getComprador())) != null){
            scoreboardDisplayer.showActualScoreboard(player);
        }
        if((player = Bukkit.getPlayer(transacion.getVendedor())) != null){
            scoreboardDisplayer.showActualScoreboard(player);
        }
    }
}
