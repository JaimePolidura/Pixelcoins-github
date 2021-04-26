package es.serversurvival.legacy.task;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.nfs.bolsa.llamadasapi.LlamadasApi;
import es.serversurvival.nfs.utils.Funciones;

@Task(period = 3 * BukkitTimeUnit.MINUTE)
public class ActualizarLlamadasApi implements TaskRunner {
    private final LlamadasApi llamadasApi = LlamadasApi.INSTANCE;

    @Override
    public void run() {
        Funciones.POOL.submit(llamadasApi::actualizarPrecios);
    }
}
