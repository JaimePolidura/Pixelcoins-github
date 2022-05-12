package es.serversurvival.bolsa.posicionesabiertas.splitaccionestask;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;

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
