package es.serversurvival.task;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.mySQL.LlamadasApi;
import es.serversurvival.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@Task(period = 3 * BukkitTimeUnit.MINUTE)
public class ActualizarLlamadasApi implements TaskRunner {
    private final LlamadasApi llamadasApi = LlamadasApi.INSTANCE;

    @Override
    public void run() {
        Funciones.POOL.submit(llamadasApi::actualizarPrecios);
    }
}
