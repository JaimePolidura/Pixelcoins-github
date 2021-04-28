package es.serversurvival.bolsa.posicionesabiertas.tasks;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionesAbiertas;

@Task(value = BukkitTimeUnit.DAY, delay = 2 * BukkitTimeUnit.MINUTE)
public class PagarDividendosTask implements TaskRunner {
    @Override
    public void run() {
        PosicionesAbiertas.INSTANCE.pagarDividendos();
    }
}
