package es.serversurvival.task;

import es.serversurvival.mySQL.Deudas;
import org.bukkit.scheduler.BukkitRunnable;

public class PagarDeudas extends BukkitRunnable {
    private Deudas deudasMySQL = Deudas.INSTANCE;

    @Override
    public void run() {
        deudasMySQL.pagarDeudas();
    }
}
