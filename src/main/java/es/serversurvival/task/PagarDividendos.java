package es.serversurvival.task;

import es.serversurvival.mySQL.PosicionesAbiertas;
import org.bukkit.scheduler.BukkitRunnable;

public class PagarDividendos extends BukkitRunnable {
    private PosicionesAbiertas posicionesAbiertasMySQL = PosicionesAbiertas.INSTANCE;

    @Override
    public void run() {
        posicionesAbiertasMySQL.pagarDividendos();
    }
}