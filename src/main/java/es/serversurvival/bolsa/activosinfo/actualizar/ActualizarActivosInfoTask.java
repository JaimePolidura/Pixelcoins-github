package es.serversurvival.bolsa.activosinfo.actualizar;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.Bukkit;

@Task(3 * BukkitTimeUnit.MINUTE)
public class ActualizarActivosInfoTask implements TaskRunner {
    private final ActualizarActivosInfoUseCase actualizarActivosInfo;

    public ActualizarActivosInfoTask() {
        this.actualizarActivosInfo = DependecyContainer.get(ActualizarActivosInfoUseCase.class);
    }

    @Override
    public void run() {
        if(!Bukkit.getServer().getOnlinePlayers().isEmpty() || this.actualizarActivosInfo.isLoading()){
            this.actualizarActivosInfo.actualizrar();
        }
    }
}
