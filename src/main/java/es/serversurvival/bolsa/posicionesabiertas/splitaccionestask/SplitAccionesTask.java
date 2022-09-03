package es.serversurvival.bolsa.posicionesabiertas.splitaccionestask;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import lombok.AllArgsConstructor;

@Task(value = BukkitTimeUnit.MINUTE, delay = BukkitTimeUnit.DAY)
@AllArgsConstructor
public class SplitAccionesTask implements TaskRunner {
    private final SplitAccionesUseCase useCase;

    @Override
    public void run() {
        useCase.actualizarSplits();
    }
}
