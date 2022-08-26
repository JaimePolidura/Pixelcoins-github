package es.serversurvival.bolsa.posicionesabiertas.splitaccionestask;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;

@Task(value = BukkitTimeUnit.MINUTE, delay = BukkitTimeUnit.DAY)
public class SplitAccionesTask implements TaskRunner {
    private final SplitAccionesUseCase useCase;

    public SplitAccionesTask() {
        this.useCase = new SplitAccionesUseCase();
    }

    @Override
    public void run() {
        useCase.actualizarSplits();
    }
}
