package es.serversurvival.bolsa.ordenespremarket.ejecutarordenes;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival._shared.utils.Funciones;

import static es.serversurvival._shared.utils.Funciones.mercadoEstaAbierto;

@Task(value = BukkitTimeUnit.MINUTE, delay = BukkitTimeUnit.MINUTE)
public class EjecutarOrdenesPreMarketTask implements TaskRunner {
    private final EjecutarOrdenesPreMarketUseCase useCase;

    public EjecutarOrdenesPreMarketTask() {
        this.useCase = new EjecutarOrdenesPreMarketUseCase();
    }

    @Override
    public void run() {
        if (mercadoEstaAbierto()) {
            useCase.ejecutarOrdenes();
        }
    }
}
