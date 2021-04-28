package es.serversurvival.bolsa.ordenespremarket.tasks;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.bolsa.ordenespremarket.mysql.OrdenesPreMarket;
import es.serversurvival.utils.Funciones;

@Task(value = BukkitTimeUnit.MINUTE, delay = BukkitTimeUnit.MINUTE)
public class EjecutarOrdenesTask implements TaskRunner {
    @Override
    public void run() {
        if (Funciones.mercadoEstaAbierto()) {
            OrdenesPreMarket.INSTANCE.ejecutarOrdenes();
        }
    }
}
