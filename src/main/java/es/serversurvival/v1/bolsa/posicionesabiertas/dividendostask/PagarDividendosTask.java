package es.serversurvival.v1.bolsa.posicionesabiertas.dividendostask;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import lombok.AllArgsConstructor;

@Task(value = BukkitTimeUnit.MINUTE, delay = 2 * BukkitTimeUnit.DAY)
@AllArgsConstructor
public class PagarDividendosTask implements TaskRunner {
    private final PagarDividendosUseCase useCase;

    @Override
    public void run() {
        useCase.pagarDividendos();
    }
}
