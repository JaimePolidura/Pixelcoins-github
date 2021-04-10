package es.serversurvival.task;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.mySQL.Empleados;
import org.bukkit.scheduler.BukkitRunnable;

@Task(period = BukkitTimeUnit.DAY)
public class PagarSueldos implements TaskRunner {
    private final Empleados empleadosMySQL = Empleados.INSTANCE;

    @Override
    public void run() {
        empleadosMySQL.pagarSueldos();
    }
}
