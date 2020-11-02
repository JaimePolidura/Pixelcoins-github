package es.serversurvival.task;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.scoreboeards.BolsaScoreboard;
import es.serversurvival.scoreboeards.DeudasDisplayScoreboard;
import es.serversurvival.scoreboeards.StatsDisplayScoreboard;
import es.serversurvival.scoreboeards.TopPlayerDisplayScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

public final class ScoreBoardManager extends BukkitRunnable {
    public static final int scoreboardSwitchDelay = 60;
    private static STATE state = STATE.START;
    private static ScoreBoardManager instance;

    private ScoreBoardManager() { }

    public static ScoreBoardManager getInstance () {
        if(instance == null)
            instance = new ScoreBoardManager();

        return instance;
    }

    private enum STATE {
        PLAYER_STATS,
        PLAYER_DEUDAS,
        TOP_PLAYERS,
        BOLSA_STATS,
        START;
    }

    @Override
    public void run() {
        switch (state) {
            case PLAYER_STATS:
                state = STATE.TOP_PLAYERS;
                break;
            case TOP_PLAYERS:
                state = STATE.BOLSA_STATS;
                break;
            case BOLSA_STATS:
                state = STATE.PLAYER_DEUDAS;
            case PLAYER_DEUDAS:
            case START:
                state = STATE.PLAYER_STATS;
                break;
        }

        updateAll(state);
    }

    private void updateAll(STATE state) {
        MySQL.conectar();

        Scoreboard scoreboard = null;
        if (state == STATE.TOP_PLAYERS) {
            scoreboard = new TopPlayerDisplayScoreboard().createScorebord();
        }

        List<Player> onlinePlayers = (List<Player>) Bukkit.getOnlinePlayers();
        for (Player player : onlinePlayers) {
            switch (state) {
                case PLAYER_STATS:
                    scoreboard = new StatsDisplayScoreboard().createScoreborad(player.getName());
                    break;
                case PLAYER_DEUDAS:
                    scoreboard = new DeudasDisplayScoreboard().createScoreborad(player.getName());
                    break;
                case BOLSA_STATS:
                    scoreboard = new BolsaScoreboard().createScoreborad(player.getName());
            }
            player.setScoreboard(scoreboard);
        }

        MySQL.desconectar();
    }

    public static void updateScoreboard(Player player) {
        MySQL.conectar();

        Scoreboard scoreboard = null;
        switch (state) {
            case START:
            case PLAYER_STATS:
                scoreboard = new StatsDisplayScoreboard().createScoreborad(player.getName());
                break;
            case TOP_PLAYERS:
                scoreboard = new TopPlayerDisplayScoreboard().createScorebord();
                break;
            case PLAYER_DEUDAS:
                scoreboard = new DeudasDisplayScoreboard().createScoreborad(player.getName());
                break;
            case BOLSA_STATS:
                scoreboard = new BolsaScoreboard().createScoreborad(player.getName());
                break;
        }
        MySQL.desconectar();

        player.setScoreboard(scoreboard);
    }
}