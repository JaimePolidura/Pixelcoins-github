package es.serversurvival.bolsa._shared.ordenespremarket.ejecutarordenes;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.shared.utils.Funciones;

@Task(value = BukkitTimeUnit.MINUTE, delay = BukkitTimeUnit.MINUTE)
public class EjecutarOrdenesPreMarketTask implements TaskRunner {
    @Override
    public void run() {
        if (Funciones.mercadoEstaAbierto()) {
            EjecutarOrdenesPreMarketUseCase.INSTANCE.ejecutarOrdenes();
        }
    }
}
