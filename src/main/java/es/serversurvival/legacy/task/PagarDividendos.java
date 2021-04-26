package es.serversurvival.legacy.task;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.nfs.bolsa.posicionesabiertas.mysql.PosicionesAbiertas;

@Task(period = BukkitTimeUnit.DAY, delay = 2 * BukkitTimeUnit.MINUTE)
public class PagarDividendos implements TaskRunner {
    @Override
    public void run() {
        PosicionesAbiertas.INSTANCE.pagarDividendos();
    }
}
