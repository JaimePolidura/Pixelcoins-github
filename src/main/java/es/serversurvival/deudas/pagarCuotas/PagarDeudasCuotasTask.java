package es.serversurvival.deudas.pagarCuotas;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;

@Task(BukkitTimeUnit.DAY)
public final class PagarDeudasCuotasTask implements TaskRunner {
    private final PagarDeudasCuotasTask pagarDeudasCuotasTask;

    public PagarDeudasCuotasTask() {
        this.pagarDeudasCuotasTask = new PagarDeudasCuotasTask();
    }

    @Override
    public void run() {
        this.pagarDeudasCuotasTask.run();
    }
}
