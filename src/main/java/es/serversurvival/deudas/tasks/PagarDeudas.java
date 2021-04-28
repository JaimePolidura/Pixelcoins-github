package es.serversurvival.deudas.tasks;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.deudas.mysql.Deudas;

@Task(BukkitTimeUnit.DAY)
public final class PagarDeudas implements TaskRunner {
    private final Deudas deudasMySQL = Deudas.INSTANCE;

    @Override
    public void run() {
        deudasMySQL.pagarDeudas();
    }
}
