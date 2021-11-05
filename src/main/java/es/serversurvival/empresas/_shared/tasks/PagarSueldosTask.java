package es.serversurvival.empresas._shared.tasks;

import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.empresas._shared.mysql.Empresa;

import java.util.List;

import static es.jaimetruman.task.BukkitTimeUnit.*;

@Task(value = DAY, delay = 20*SECOND)
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
