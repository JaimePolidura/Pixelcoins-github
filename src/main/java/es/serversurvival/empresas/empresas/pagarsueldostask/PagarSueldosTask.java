package es.serversurvival.empresas.empresas.pagarsueldostask;

import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;

import java.util.List;

import static es.jaimetruman.task.BukkitTimeUnit.*;

@Task(value = DAY, delay = 20*SECOND)
public final class PagarSueldosTask implements TaskRunner, AllMySQLTablesInstances {
    private final EmpresasService empresasService;
    private final PagarSueldosUseCase useCase;

    public PagarSueldosTask() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.useCase = new PagarSueldosUseCase();
    }

    @Override
    public void run() {
        List<Empresa> allEmpresas = empresasService.getAll();

        for (Empresa empresa : allEmpresas) {
            useCase.pagarSueldos(empresa, empleadosMySQL.getEmpleadosEmrpesa(empresa.getNombre()));
        }
    }
}
