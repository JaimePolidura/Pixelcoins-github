package es.serversurvival.deudas.pagarCuota;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.deudas.mysql.Deudas;

@Task(BukkitTimeUnit.DAY)
public final class PagarDeudasCuotasTask implements TaskRunner {
    private final PagarDeudasCuotasUseCase useCase = PagarDeudasCuotasUseCase.INSTANCE;

    @Override
    public void run() {
        useCase.pagarDeudas();
    }
}
