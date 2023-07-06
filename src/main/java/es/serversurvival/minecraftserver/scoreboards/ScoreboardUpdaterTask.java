package es.serversurvival.minecraftserver.scoreboards;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.hooks.AfterAllScanned;
import es.jaime.javaddd.application.utils.CollectionUtils;
import es.serversurvival.minecraftserver.scoreboards.displays.PatrimonioDisplayScoreboard;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Task(value = BukkitTimeUnit.SECOND * 20, delay = 0)
public final class ScoreboardUpdaterTask implements TaskRunner, AfterAllScanned {
    private final ScoreboardDisplayer scoreboardDisplayer;

    private Iterator<ServerScoreboardCreator> nextScoreboard;
    private List<ServerScoreboardCreator> scoreboards;

    @Override
    public void run() {
        scoreboardDisplayer.showNewScoreboard((List<Player>) Bukkit.getOnlinePlayers(), nextScoreboard.next());
    }

    @Override
    public void afterAllScanned(DependenciesRepository dependencies) {
        this.scoreboards = dependencies.filterByImplementsInterface(ServerScoreboardCreator.class);
        this.nextScoreboard = CollectionUtils.newCircularIterator(scoreboards);
    }
}
