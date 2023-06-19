package es.serversurvival.minecraftserver.scoreboards;

import es.dependencyinjector.dependencies.annotations.Service;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

@Service
public final class ScoreboardDisplayer implements Listener {
    private ServerScoreboardCreator lastScoreboardCreatorSelected;

    public void showNewScoreboard(List<Player> players, ServerScoreboardCreator scoreboardCreator) {
        if(players.isEmpty())
            return;

        this.lastScoreboardCreatorSelected = scoreboardCreator;

        if(scoreboardCreator.isGlobal()){
            showGlobalScoreboard(scoreboardCreator);
        }else{
            showSingleScoreboard(scoreboardCreator);
        }
    }

    public void showActualScoreboard(Player player) {
        if(lastScoreboardCreatorSelected != null){ //Puede ser que ScoreboardUpdaterTask no se haya ejecutado
            player.setScoreboard(this.lastScoreboardCreatorSelected.create(player));
        }
    }

    private void showGlobalScoreboard(ServerScoreboardCreator actualScoreboard) {
        List<Player> onlinePlayers = (List<Player>) Bukkit.getOnlinePlayers();

        Scoreboard newScoreboard = actualScoreboard.create(null);
        onlinePlayers.forEach(ply -> ply.setScoreboard(newScoreboard));
    }

    private void showSingleScoreboard(ServerScoreboardCreator scoreboardCreator) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard newScoreboard = scoreboardCreator.create(player);
            player.setScoreboard(newScoreboard);
        }
    }
}
