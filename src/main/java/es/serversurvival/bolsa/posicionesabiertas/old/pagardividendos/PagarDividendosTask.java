package es.serversurvival.bolsa.posicionesabiertas.old.pagardividendos;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;

@Task(value = BukkitTimeUnit.DAY, delay = 2 * BukkitTimeUnit.MINUTE)
public class PagarDividendosTask implements TaskRunner {
    private final PagarDividendosUseCase useCase = PagarDividendosUseCase.INSTANCE;

    @Override
    public void run() {
        useCase.pagarDividendos();
    }
}
