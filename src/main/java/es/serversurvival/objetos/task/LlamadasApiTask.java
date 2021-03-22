package es.serversurvival.objetos.task;

import es.serversurvival.objetos.mySQL.LlamadasApi;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class LlamadasApiTask extends BukkitRunnable {
    private LlamadasApi llamadasApi = new LlamadasApi();
    public final static int delay = 600;

    @Override
    public void run() {
        llamadasApi.actualizar(Bukkit.getServer());
    }
}
