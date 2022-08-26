package es.serversurvival.deudas.pagarCuotas;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;

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
