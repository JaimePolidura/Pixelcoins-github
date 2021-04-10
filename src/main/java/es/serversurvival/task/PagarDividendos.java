package es.serversurvival.task;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.mySQL.PosicionesAbiertas;
import org.bukkit.scheduler.BukkitRunnable;

@Task(period = BukkitTimeUnit.DAY, delay = 2 * BukkitTimeUnit.MINUTE)
public class PagarDividendos implements TaskRunner {
    @Override
    public void run() {
        PosicionesAbiertas.INSTANCE.pagarDividendos();
    }
}
