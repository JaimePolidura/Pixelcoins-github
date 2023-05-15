package es.serversurvival.v1._shared.scoreboards;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.hooks.AfterAllScanned;
import es.jaime.javaddd.application.utils.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Task(value = BukkitTimeUnit.MINUTE, delay = BukkitTimeUnit.MINUTE)
public final class ScoreboardUpdaterTask extends BukkitRunnable implements AfterAllScanned {
    private final ScoreboardDisplayer scoreBoardManager;

    private Iterator<ServerScoreboardCreator> nextScoreboard;
    private List<ServerScoreboardCreator> scoreboards;

    @Override
    public void run() {
        scoreBoardManager.showNewScoreboard((List<Player>) Bukkit.getOnlinePlayers(), nextScoreboard.next());
    }

    @Override
    public void afterAllScanned(DependenciesRepository dependenciesRepository) {
        this.scoreboards = dependenciesRepository.queryByImplementsInterface(ServerScoreboardCreator.class);
        this.nextScoreboard = CollectionUtils.newCircularIterator(scoreboards);
    }
}
