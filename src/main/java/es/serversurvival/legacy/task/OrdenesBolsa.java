package es.serversurvival.legacy.task;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.legacy.mySQL.OrdenesPreMarket;

@Task(period = BukkitTimeUnit.MINUTE, delay = BukkitTimeUnit.MINUTE)
public class OrdenesBolsa implements TaskRunner {
    @Override
    public void run() {
        OrdenesPreMarket.INSTANCE.ejecutarOrdenes();
    }
}
