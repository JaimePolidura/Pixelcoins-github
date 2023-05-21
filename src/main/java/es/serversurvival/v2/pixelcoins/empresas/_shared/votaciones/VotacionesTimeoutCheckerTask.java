package es.serversurvival.v2.pixelcoins.empresas._shared.votaciones;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.votaciones.EstadoVotacion;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.votaciones.VotacionesService;
import lombok.AllArgsConstructor;

@Task(BukkitTimeUnit.DAY)
@AllArgsConstructor
public final class VotacionesTimeoutCheckerTask implements TaskRunner {
    private final ResultadoVotacionChecker resultadoVotacionChecker;
    private final VotacionesService votacionesService;

    @Override
    public void run() {
        votacionesService.findAll().stream()
                .filter(votacion -> votacion.getEstado() == EstadoVotacion.ABIERTA)
                .forEach(resultadoVotacionChecker::check);
    }
}
