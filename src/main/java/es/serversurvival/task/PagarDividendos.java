package es.serversurvival.task;

import es.serversurvival.mySQL.PosicionesAbiertas;
import org.bukkit.scheduler.BukkitRunnable;

public class PagarDividendos extends BukkitRunnable {
    @Override
    public void run() {
        PosicionesAbiertas.INSTANCE.pagarDividendos();
    }
}
