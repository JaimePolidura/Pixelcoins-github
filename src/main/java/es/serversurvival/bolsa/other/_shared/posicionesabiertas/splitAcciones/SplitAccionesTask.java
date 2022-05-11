package es.serversurvival.bolsa.other._shared.posicionesabiertas.splitAcciones;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;

@Task(value = BukkitTimeUnit.DAY, delay = BukkitTimeUnit.MINUTE)
public class SplitAccionesTask implements TaskRunner {
    private final SplitAccionesUseCase useCase = SplitAccionesUseCase.INSTANCE;

    @Override
    public void run() {
        useCase.actualizarSplits();
    }
}
