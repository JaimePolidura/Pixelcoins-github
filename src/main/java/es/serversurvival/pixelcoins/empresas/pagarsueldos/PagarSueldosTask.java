package es.serversurvival.pixelcoins.empresas.pagarsueldos;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Task(value = BukkitTimeUnit.DAY, delay = BukkitTimeUnit.DAY)
public final class PagarSueldosTask implements TaskRunner {
    private final EmpresasService empresasService;
    private final UseCaseBus useCaseBus;

    @Override
    public void run() {
        this.empresasService.findAllNoCerradas().stream()
                .filter(empresa -> !empresa.isEstaCerrado())
                .forEach(empresa -> useCaseBus.handle(PagadorSueldosEmpresaParametros.from(empresa)));
    }
}
