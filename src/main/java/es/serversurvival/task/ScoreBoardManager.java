package es.serversurvival.task;

import es.jaime.EventListener;
import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.mySQL.eventos.TransaccionEvent;
import es.serversurvival.scoreboeards.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Task(period = BukkitTimeUnit.MINUTE)
public final class ScoreBoardManager implements TaskRunner, Listener {
    private final List<ServerScoreboard> scoreboards;
    private int actualIndex;

    public ScoreBoardManager() {
        this.scoreboards = new ArrayList<>();
        scoreboards.add(new StatsDisplayScoreboard());
        scoreboards.add(new TopPlayerDisplayScoreboard());
        scoreboards.add(new DeudasDisplayScoreboard());
        scoreboards.add(new BolsaScoreboard());
    }

    @EventListener
    public void onPixelcoinTransaccion (TransaccionEvent event) {
        update(Bukkit.getPlayer(event.getComprador()), Bukkit.getPlayer(event.getVendedor()));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evento) {
        update(evento.getPlayer());
    }

    @Override
    public void run() {
        if(actualIndex + 1 >= scoreboards.size()){
            this.actualIndex = 0;
        }else{
            this.actualIndex++;
        }

        update(Bukkit.getOnlinePlayers());
    }

    public void update (Player... players) {
        update(Arrays.asList(players));
    }

    public void update (Collection<? extends Player> players) {
        ServerScoreboard actualScoreboard = scoreboards.get(actualIndex);

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
        players.forEach(player -> {
            if(player != null){
                Scoreboard newScoreboard = ((SingleScoreboard) actualScoreboard).createScoreborad(player.getName());
                player.setScoreboard(newScoreboard);
            }
        });
    }
}
