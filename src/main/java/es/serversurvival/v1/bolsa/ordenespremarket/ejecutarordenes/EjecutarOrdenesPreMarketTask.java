package es.serversurvival.v1.bolsa.ordenespremarket.ejecutarordenes;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import es.serversurvival.v1._shared.utils.Funciones;
import lombok.AllArgsConstructor;

@Task(value = BukkitTimeUnit.MINUTE, delay = BukkitTimeUnit.MINUTE)
@AllArgsConstructor
public class EjecutarOrdenesPreMarketTask implements TaskRunner {
    private final EjecutarOrdenesPreMarketUseCase useCase;

    @Override
    public void run() {
        if (Funciones.mercadoEstaAbierto() && !useCase.isLoading()) {
            useCase.ejecutarOrdenes();
        }
    }
}
