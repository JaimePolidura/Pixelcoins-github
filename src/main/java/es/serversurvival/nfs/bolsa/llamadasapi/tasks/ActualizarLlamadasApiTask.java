package es.serversurvival.nfs.bolsa.llamadasapi.tasks;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.nfs.bolsa.llamadasapi.mysql.LlamadasApi;
import es.serversurvival.nfs.utils.Funciones;

@Task(3 * BukkitTimeUnit.MINUTE)
public class ActualizarLlamadasApiTask implements TaskRunner {
    private final LlamadasApi llamadasApi = LlamadasApi.INSTANCE;

    @Override
    public void run() {
        llamadasApi.actualizarPrecios();
    }
}
