package es.serversurvival.minecraftserver.scoreboards.listeners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival.Pixelcoin;
import es.serversurvival.minecraftserver.scoreboards.ScoreboardDisplayer;
import es.serversurvival.minecraftserver.scoreboards.displays.PatrimonioDisplayScoreboard;
import es.serversurvival.pixelcoins.jugadores.patrimonio.TipoCuentaPatrimonio;
import es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.domain.Transaccion;
import es.serversurvival.pixelcoins.transacciones.domain.TransaccionCreada;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLOutput;
import java.util.UUID;

@EventHandler
@RequiredArgsConstructor
public final class OnTransactionScoreboard {
    private final ScoreboardDisplayer scoreboardDisplayer;

    @EventListener(pritority = Priority.LOWEST)
    public void on(TransaccionCreada transaccionCreada) {
        Transaccion transaccion = transaccionCreada.getTransaccion();

        if(scoreboardDisplayer.getLastScoreboardCreatorSelected() instanceof PatrimonioDisplayScoreboard patrimonioScoreboard) {
            updateTipoCuentaScoreboard(patrimonioScoreboard, transaccion.getPagadorId(), transaccion.getTipo().getCuentaAfectadaPagador(),
                    transaccion.getPixelcoins());
            updateTipoCuentaScoreboard(patrimonioScoreboard, transaccion.getPagadoId(), transaccion.getTipo().getCuentaAfectadaPagado(),
                    transaccion.getPixelcoins());
        }
    }

    private void updateTipoCuentaScoreboard(PatrimonioDisplayScoreboard scoreboard, UUID entidadId,
                                            TipoTransaccion.CuentaAfectada cuentaAfectada, double pixelcoins) {
        Player player = Bukkit.getPlayer(entidadId);
        if(player == null){
            return;
        }

        TipoCuentaPatrimonio tipoCuentaHaber = cuentaAfectada.getHaber();
        TipoCuentaPatrimonio tipoCuentaDebe = cuentaAfectada.getDebe();

        if(tipoCuentaDebe != null){
            setScoreboardLine(scoreboard, player, tipoCuentaDebe, tipoCuentaDebe.isEsActivo() ? pixelcoins : -1 * pixelcoins);
        }
        if(tipoCuentaHaber != null) {
            setScoreboardLine(scoreboard, player, tipoCuentaHaber, tipoCuentaHaber.isEsActivo() ? -1 * pixelcoins : pixelcoins);
        }
    }

    private void setScoreboardLine(PatrimonioDisplayScoreboard scoreboard, Player player,
                                          TipoCuentaPatrimonio tipoCuentaDebe, double pixelcoins) {

        Bukkit.getScheduler().runTask(Pixelcoin.INSTANCE, () -> {
            scoreboard.updateTipoCuenta(player, tipoCuentaDebe, pixelcoins);
        });
    }
}
