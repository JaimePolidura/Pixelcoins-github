package es.serversurvival.nfs.bolsa.ordenespremarket.tasks;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.nfs.bolsa.ordenespremarket.mysql.OrdenesPreMarket;
import es.serversurvival.nfs.utils.Funciones;

import static es.serversurvival.nfs.utils.Funciones.*;

@Task(value = BukkitTimeUnit.MINUTE, delay = BukkitTimeUnit.MINUTE)
public class EjecutarOrdenesTask implements TaskRunner {
    @Override
    public void run() {
        if (mercadoEstaAbierto()) {
            OrdenesPreMarket.INSTANCE.ejecutarOrdenes();
        }
    }
}
