package es.serversurvival.bolsa.llamadasapi.actualizarprecios;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.bolsa.llamadasapi.mysql.LlamadasApi;

@Task(3 * BukkitTimeUnit.MINUTE)
public class ActualizarLlamadasApiTask implements TaskRunner {
    private final ActualizarPreciosUseCase useCase = ActualizarPreciosUseCase.INSTANCE;

    @Override
    public void run() {
        useCase.actualizarPrecios();
    }
}
