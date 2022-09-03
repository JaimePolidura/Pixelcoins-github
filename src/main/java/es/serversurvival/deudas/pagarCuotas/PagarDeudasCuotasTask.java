package es.serversurvival.deudas.pagarCuotas;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import lombok.AllArgsConstructor;

@Task(BukkitTimeUnit.DAY)
@AllArgsConstructor
public final class PagarDeudasCuotasTask implements TaskRunner {
    private final PagarDeudasCuotasUseCase pagarDeudasCuotasUseCase;

    @Override
    public void run() {
        this.pagarDeudasCuotasUseCase.pagarDeudas();
    }
}
