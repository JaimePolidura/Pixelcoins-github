package es.serversurvival.task;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.scoreboeards.*;
import net.minecraft.server.v1_16_R1.DoubleBlockFinder;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public final class ScoreBoardManager extends BukkitRunnable {
    private List<ServerScoreboard> scoreboards;
    private int actualIndex;

    public static final int scoreboardSwitchDelay = 60;
    private static ScoreBoardManager instance;

    private ScoreBoardManager() {
        this.scoreboards = new ArrayList<>();
        scoreboards.add(new StatsDisplayScoreboard());
        scoreboards.add(new TopPlayerDisplayScoreboard());
        scoreboards.add(new DeudasDisplayScoreboard());
        scoreboards.add(new BolsaScoreboard());
    }

    public static ScoreBoardManager getInstance () {
        if(instance == null)
            instance = new ScoreBoardManager();

        return instance;
    }

    @Override
    public void run() {
        if(actualIndex + 1 >= scoreboards.size()){
            this.actualIndex = 0;
        }else{
            this.actualIndex++;
        }

        updateAll(scoreboards.get(actualIndex));
    }

    private void updateAll(ServerScoreboard serverScoreboard) {
        MySQL.conectar();

        List<Player> onlinePlayers = (List<Player>) Bukkit.getOnlinePlayers();

        if(serverScoreboard instanceof SingleScoreboard){
            SingleScoreboard singleScoreboard = (SingleScoreboard) serverScoreboard;
            onlinePlayers.forEach(player -> player.setScoreboard(singleScoreboard.createScoreborad(player.getName())));

        }else{
            GlobalScoreboard globalScoreboard = (GlobalScoreboard) serverScoreboard;
            Scoreboard scoreboard = globalScoreboard.createScorebord();
            onlinePlayers.forEach((player -> player.setScoreboard(scoreboard)));
        }

        MySQL.desconectar();
    }

    public void updateScoreboard(Player player) {
        MySQL.conectar();
        ServerScoreboard actualScoreboard = scoreboards.get(actualIndex);

        if(actualScoreboard instanceof SingleScoreboard){
            Scoreboard newScoreboard = ((SingleScoreboard) actualScoreboard).createScoreborad(player.getName());
            player.setScoreboard(newScoreboard);
        }else{
            List<Player> onlinePlayers = (List<Player>) Bukkit.getOnlinePlayers();
            Scoreboard newScoreboard = ((GlobalScoreboard) actualScoreboard).createScorebord();
            onlinePlayers.forEach(ply -> ply.setScoreboard(newScoreboard));
        }

        MySQL.desconectar();
    }
}
