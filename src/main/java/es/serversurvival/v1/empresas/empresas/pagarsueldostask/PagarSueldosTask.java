package es.serversurvival.v1.empresas.empresas.pagarsueldostask;

import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import es.serversurvival.v1.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.v1.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.v1.empresas.empresas._shared.domain.Empresa;
import lombok.AllArgsConstructor;

import java.util.List;

import static es.bukkitclassmapper.task.BukkitTimeUnit.DAY;
import static es.bukkitclassmapper.task.BukkitTimeUnit.SECOND;

@Task(value = DAY, delay = 20 * SECOND)
@AllArgsConstructor
public final class PagarSueldosTask implements TaskRunner {
    private final EmpresasService empresasService;
    private final EmpleadosService empleadosService;
    private final PagarSueldosUseCase useCase;

    @Override
    public void run() {
        List<Empresa> allEmpresas = empresasService.findAll();

        for (Empresa empresa : allEmpresas) {
            useCase.pagarSueldos(empresa, empleadosService.findByEmpresa(empresa.getNombre()));
        }
    }
}
