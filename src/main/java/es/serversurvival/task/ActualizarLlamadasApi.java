package es.serversurvival.task;

import es.serversurvival.mySQL.LlamadasApi;
import es.serversurvival.util.Funciones;
import org.bukkit.scheduler.BukkitRunnable;

public class ActualizarLlamadasApi extends BukkitRunnable {
    private LlamadasApi llamadasApi = LlamadasApi.INSTANCE;

    @Override
    public void run() {
        Funciones.POOL.submit(() -> llamadasApi.actualizarPrecios());
    }
}
