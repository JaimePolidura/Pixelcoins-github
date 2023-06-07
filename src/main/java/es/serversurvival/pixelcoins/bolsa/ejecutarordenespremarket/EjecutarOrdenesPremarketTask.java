package es.serversurvival.pixelcoins.bolsa.ejecutarordenespremarket;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import es.serversurvival.pixelcoins.bolsa._shared.premarket.application.AbridorOrdenesPremarket;
import es.serversurvival.pixelcoins.bolsa._shared.premarket.application.OrdenesPremarketService;
import lombok.AllArgsConstructor;

import static es.jaime.javaddd.application.utils.ExceptionUtils.*;

@Task(BukkitTimeUnit.MINUTE * 5)
@AllArgsConstructor
public final class EjecutarOrdenesPremarketTask implements TaskRunner {
    private final EjecutadorOrdenPremarket ejecutadorOrdenPremarket;
    private final OrdenesPremarketService ordenesPremarketService;
    private final AbridorOrdenesPremarket abridorOrdenesPremarket;

    @Override
    public void run() {
        if(!abridorOrdenesPremarket.estaElMercadoAbierto()){
            return;
        }

        ordenesPremarketService.findAll()
                .forEach(ordenPremarket -> ignoreException(() -> {
                    ejecutadorOrdenPremarket.ejecutar(ordenPremarket);
                }));
    }
}
