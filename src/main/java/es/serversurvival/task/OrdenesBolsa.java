package es.serversurvival.task;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.Ordenes;
import org.bukkit.scheduler.BukkitRunnable;

public class OrdenesBolsa extends BukkitRunnable {
    @Override
    public void run() {
        Ordenes.INSTANCE.ejecutarOrdenes();
    }
}
