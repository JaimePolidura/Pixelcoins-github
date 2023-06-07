package es.serversurvival.pixelcoins.deudas.pagarcuotas;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import es.serversurvival.pixelcoins.deudas._shared.DeudasService;
import es.serversurvival.pixelcoins.deudas._shared.EstadoDeuda;
import lombok.AllArgsConstructor;

@Task(value = BukkitTimeUnit.DAY)
@AllArgsConstructor
public final class PagarCuotasTask implements TaskRunner {
    private final PagadorDeudaCuotas pagadorDeudaCuotas;
    private final DeudasService deudasService;

    @Override
    public void run() {
        this.deudasService.findAll().stream()
                .filter(deuda -> deuda.getEstadoDeuda() == EstadoDeuda.PENDIENTE)
                .forEach(this.pagadorDeudaCuotas::pagarCuotas);
    }
}
