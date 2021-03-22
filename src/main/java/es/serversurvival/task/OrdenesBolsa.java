package es.serversurvival.task;

import es.serversurvival.mySQL.OrdenesPreMarket;
import org.bukkit.scheduler.BukkitRunnable;

public class OrdenesBolsa extends BukkitRunnable {
    @Override
    public void run() {
        OrdenesPreMarket.INSTANCE.ejecutarOrdenes();
    }
}
