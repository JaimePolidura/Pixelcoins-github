package es.serversurvival.task;

import es.serversurvival.mySQL.Empleados;
import org.bukkit.scheduler.BukkitRunnable;

public class PagarSueldos extends BukkitRunnable {
    private Empleados empleadosMySQL = Empleados.INSTANCE;

    @Override
    public void run() {
        empleadosMySQL.pagarSueldos();
    }
}
