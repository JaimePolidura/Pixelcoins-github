package es.serversurvival.empresas.empresas.pagarsueldostask;

import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;

import java.util.List;

import static es.jaimetruman.task.BukkitTimeUnit.*;

@Task(value = DAY, delay = 20*SECOND)
public final class PagarSueldosTask implements TaskRunner {
    private final EmpresasService empresasService;
    private final EmpleadosService empleadosService;
    private final PagarSueldosUseCase useCase;

    public PagarSueldosTask() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.empleadosService = DependecyContainer.get(EmpleadosService.class);

        this.useCase = new PagarSueldosUseCase();
    }

    @Override
    public void run() {
        List<Empresa> allEmpresas = empresasService.findAll();

        for (Empresa empresa : allEmpresas) {
            useCase.pagarSueldos(empresa, empleadosService.findByEmpresa(empresa.getNombre()));
        }
    }
}
