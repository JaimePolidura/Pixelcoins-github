package es.serversurvival.task;

import es.serversurvival.mySQL.Empleados;
import es.serversurvival.mySQL.MySQL;
import org.bukkit.scheduler.BukkitRunnable;

public class PagarSueldos extends BukkitRunnable {
    private Empleados empleadosMySQL = Empleados.INSTANCE;

    @Override
    public void run() {
        MySQL.conectar();
        empleadosMySQL.pagarSueldos();
    }
}