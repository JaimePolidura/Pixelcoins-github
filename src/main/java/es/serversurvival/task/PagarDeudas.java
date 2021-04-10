package es.serversurvival.task;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.mySQL.Deudas;
import org.bukkit.scheduler.BukkitRunnable;

@Task(period = BukkitTimeUnit.DAY)
public class PagarDeudas implements TaskRunner {
    private final Deudas deudasMySQL = Deudas.INSTANCE;

    @Override
    public void run() {
        deudasMySQL.pagarDeudas();
    }
}
