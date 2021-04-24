package es.serversurvival.nfs.deudas.tasks;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.legacy.mySQL.Deudas;

@Task(BukkitTimeUnit.DAY)
public final class PagarDeudas implements TaskRunner {
    private final Deudas deudasMySQL = Deudas.INSTANCE;

    @Override
    public void run() {
        deudasMySQL.pagarDeudas();
    }
}
