package es.serversurvival.objetos.task;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.MySQL;
import es.serversurvival.objetos.scoreboeards.DeudasDisplayScoreboard;
import es.serversurvival.objetos.scoreboeards.StatsDisplayScoreboard;
import es.serversurvival.objetos.scoreboeards.TopPlayerDisplayScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

public class ScoreboardTaskManager extends BukkitRunnable {
    private MySQL mySQL = new MySQL();
    public static final int scoreboardSwitchDelay = 60;
    private static STATE state = STATE.START;
    private DecimalFormat formatea = new DecimalFormat("###,###.##");

    private enum STATE {
        PLAYER_STATS,
        PLAYER_DEUDAS,
        TOP_PLAYERS,
        START;
    }

    @Override
    public void run() {
        switch (state) {
            case PLAYER_STATS:
                state = STATE.TOP_PLAYERS;
                break;
            case TOP_PLAYERS:
                state = STATE.PLAYER_DEUDAS;
                break;
            case PLAYER_DEUDAS:
            case START:
                state = STATE.PLAYER_STATS;
                break;
        }

        updateAll(state);
    }

    private void updateAll(STATE state) {
        Scoreboard scoreboard = null;
        mySQL.conectar();

        if (state == STATE.TOP_PLAYERS) {
            scoreboard = new TopPlayerDisplayScoreboard().createScoreboard(Funciones.crearMapaTopPlayers(false));
        }

        List<Player> onlinePlayers = (List<Player>) Bukkit.getOnlinePlayers();
        for (Player p : onlinePlayers) {
            switch (state) {
                case PLAYER_STATS:
                    scoreboard = new StatsDisplayScoreboard().createScoreborad(p.getName());
                    break;
                case PLAYER_DEUDAS:
                    scoreboard = new DeudasDisplayScoreboard().createScoreborad(p.getName());
                    break;
            }
            p.setScoreboard(scoreboard);
        }

        mySQL.desconectar();
    }

    public void updateScoreboard(Player p) {
        mySQL.conectar();
        Scoreboard scoreboard = null;
        switch (state) {
            case PLAYER_STATS:
                scoreboard = new StatsDisplayScoreboard().createScoreborad(p.getName());
                break;
            case TOP_PLAYERS:
                scoreboard = new TopPlayerDisplayScoreboard().createScoreboard(Funciones.crearMapaTopPlayers(false));
                break;
            case PLAYER_DEUDAS:
                scoreboard = new DeudasDisplayScoreboard().createScoreborad(p.getName());
                break;
        }
        mySQL.desconectar();

        p.setScoreboard(scoreboard);
    }
}