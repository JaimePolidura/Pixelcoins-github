package es.serversurvival.bolsa.posicionesabiertas.dividendostask;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;

@Task(value = BukkitTimeUnit.MINUTE, delay = 2 * BukkitTimeUnit.DAY)
public class PagarDividendosTask implements TaskRunner {
    private final PagarDividendosUseCase useCase;

    public PagarDividendosTask() {
        this.useCase = new PagarDividendosUseCase();
    }

    @Override
    public void run() {
        useCase.pagarDividendos();
    }
}
