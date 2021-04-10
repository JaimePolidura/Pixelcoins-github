package es.serversurvival.task;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.mySQL.PosicionesAbiertas;
import org.bukkit.scheduler.BukkitRunnable;

@Task(period = BukkitTimeUnit.DAY, delay = BukkitTimeUnit.MINUTE)
public class SplitAcciones implements TaskRunner {
    private final PosicionesAbiertas posicionesAbiertasMySQL = PosicionesAbiertas.INSTANCE;

    @Override
    public void run() {
        posicionesAbiertasMySQL.actualizarSplits();
    }
}
