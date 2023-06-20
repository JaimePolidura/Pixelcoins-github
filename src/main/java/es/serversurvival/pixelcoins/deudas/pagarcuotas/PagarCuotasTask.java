package es.serversurvival.pixelcoins.deudas.pagarcuotas;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasService;
import es.serversurvival.pixelcoins.deudas._shared.domain.EstadoDeuda;
import lombok.AllArgsConstructor;

@Task(value = BukkitTimeUnit.DAY)
@AllArgsConstructor
public final class PagarCuotasTask implements TaskRunner {
    private final DeudasService deudasService;
    private final UseCaseBus useCaseBus;

    @Override
    public void run() {
        this.deudasService.findAll().stream()
                .filter(deuda -> deuda.getEstadoDeuda() == EstadoDeuda.PENDIENTE)
                .forEach(deuda -> useCaseBus.handle(PagarDeudaCuotasParametros.from(deuda)));
    }
}
