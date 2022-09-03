package es.serversurvival._shared.scoreboards;

import es.dependencyinjector.annotations.Component;
import es.serversurvival.bolsa.posicionesabiertas.vercartera.BolsaCarteraScoreboard;
import es.serversurvival.deudas.ver.DeudasDisplayScoreboard;
import es.serversurvival.jugadores.perfil.StatsDisplayScoreboard;
import es.serversurvival.jugadores.top.TopPlayerDisplayScoreboard;
import es.serversurvival.Pixelcoin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

@Component
public final class ScoreboardUpdateTask extends BukkitRunnable {
    private final List<ServerScoreboard> scoreboards;
    private final ScoreBoardManager scoreBoardManager;
    private int actualIndex;

    public ScoreboardUpdateTask() {
        this.scoreboards = new ArrayList<>();
        scoreboards.add(new StatsDisplayScoreboard());
        scoreboards.add(new TopPlayerDisplayScoreboard());
        scoreboards.add(new DeudasDisplayScoreboard());
        scoreboards.add(new BolsaCarteraScoreboard());

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
