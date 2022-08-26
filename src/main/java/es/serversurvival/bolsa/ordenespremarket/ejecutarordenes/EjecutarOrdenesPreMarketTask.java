package es.serversurvival.bolsa.ordenespremarket.ejecutarordenes;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;

import static es.serversurvival._shared.utils.Funciones.mercadoEstaAbierto;

@Task(value = BukkitTimeUnit.MINUTE, delay = BukkitTimeUnit.MINUTE)
public class EjecutarOrdenesPreMarketTask implements TaskRunner {
    private final EjecutarOrdenesPreMarketUseCase useCase;

    public EjecutarOrdenesPreMarketTask() {
        this.useCase = new EjecutarOrdenesPreMarketUseCase();
    }

    @Override
    public void run() {
        if (mercadoEstaAbierto() && !useCase.isLoading()) {
            useCase.ejecutarOrdenes();
        }
    }
}
