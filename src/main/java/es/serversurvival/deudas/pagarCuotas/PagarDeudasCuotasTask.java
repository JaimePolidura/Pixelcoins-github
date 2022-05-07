package es.serversurvival.deudas.pagarCuotas;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;

@Task(BukkitTimeUnit.DAY)
public final class PagarDeudasCuotasTask implements TaskRunner {
    private final PagarDeudasCuotasUseCase pagarDeudasCuotasUseCase;

    public PagarDeudasCuotasTask() {
        this.pagarDeudasCuotasUseCase = new PagarDeudasCuotasUseCase();
    }

    @Override
    public void run() {
        this.pagarDeudasCuotasUseCase.pagarDeudas();
    }
}
