package es.serversurvival.legacy.task;


import es.serversurvival.legacy.scoreboeards.*;
import es.serversurvival.nfs.Pixelcoin;
import es.serversurvival.scoreboeards.*;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public final class ScoreboardUpdater extends BukkitRunnable {
    private final List<ServerScoreboard> scoreboards;
    private final ScoreBoardManager scoreBoardManager;
    private int actualIndex;

    public ScoreboardUpdater() {
        this.scoreboards = new ArrayList<>();
        scoreboards.add(new StatsDisplayScoreboard());
        scoreboards.add(new TopPlayerDisplayScoreboard());
        scoreboards.add(new DeudasDisplayScoreboard());
        scoreboards.add(new BolsaScoreboard());

        this.scoreBoardManager = Pixelcoin.scoreboarManager();
    }

    @Override
    public void run() {
        if(actualIndex + 1 >= scoreboards.size()){
            this.actualIndex = 0;
        }else{
            this.actualIndex++;
        }

        scoreBoardManager.update(Bukkit.getOnlinePlayers());
    }

    public ServerScoreboard getActualScoreboard () {
        return this.scoreboards.get(actualIndex);
    }
}
