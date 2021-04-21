package es.serversurvival.task;

import es.jaime.EventListener;
import es.serversurvival.main.Pixelcoin;
import es.serversurvival.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.mySQL.tablasObjetos.Transaccion;
import es.serversurvival.scoreboeards.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class ScoreBoardManager implements Listener {
    
    @EventListener({EventoTipoTransaccion.class})
    public void onPixelcoinTransaccion (PixelcoinsEvento event) {
        Transaccion transaccion = ((EventoTipoTransaccion) event).buildTransaccion();

        update(Bukkit.getPlayer(transaccion.getComprador()), Bukkit.getPlayer(transaccion.getVendedor()));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evento) {
        update(evento.getPlayer());
    }

    public void update (Player... players) {
        update(Arrays.asList(players));
    }

    public void update (Collection<? extends Player> players) {
        ServerScoreboard actualScoreboard = Pixelcoin.scoreboardUpdater().getActualScoreboard();

        System.out.println(actualScoreboard.getClass().getName());

        if(actualScoreboard instanceof GlobalScoreboard){
            updateGlobalScoreboard(actualScoreboard);
        }else{
            updateSingleScoreboard(actualScoreboard, players);
        }
    }

    private void updateGlobalScoreboard (ServerScoreboard actualScoreboard) {
        List<Player> onlinePlayers = (List<Player>) Bukkit.getOnlinePlayers();
        Scoreboard newScoreboard = ((GlobalScoreboard) actualScoreboard).createScorebord();
        onlinePlayers.forEach(ply -> ply.setScoreboard(newScoreboard));
    }

    private void updateSingleScoreboard (ServerScoreboard actualScoreboard, Collection<? extends Player> players) {
        for (Player player : players) {
            if(player != null){
                Scoreboard newScoreboard = ((SingleScoreboard) actualScoreboard).createScoreborad(player.getName());
                player.setScoreboard(newScoreboard);
            }
        }
    }
}
