package es.serversurvival.nfs.empresas.tasks;

import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.nfs.empresas.mysql.Empresa;

import java.util.List;

import static es.jaimetruman.task.BukkitTimeUnit.*;

@Task(DAY)
public final class PagarSueldosTask implements TaskRunner, AllMySQLTablesInstances {
    private final PagarSueldosUseCase useCase = PagarSueldosUseCase.INSTANCE;

    @Override
    public void run() {
        List<Empresa> allEmpresas = empresasMySQL.getTodasEmpresas();

        for (Empresa empresa : allEmpresas) {
            useCase.pagarSueldos(empresa, empleadosMySQL.getEmpleadosEmrpesa(empresa.getNombre()));
        }
    }
}
