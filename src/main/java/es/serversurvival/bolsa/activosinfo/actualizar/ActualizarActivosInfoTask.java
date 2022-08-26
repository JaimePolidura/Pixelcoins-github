package es.serversurvival.bolsa.activosinfo.actualizar;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import org.bukkit.Bukkit;

@Task(value = 3 * BukkitTimeUnit.MINUTE, delay = BukkitTimeUnit.MINUTE)
public class ActualizarActivosInfoTask implements TaskRunner {
    private final ActualizarActivosInfoUseCase actualizarActivosInfo;

    public ActualizarActivosInfoTask() {
        this.actualizarActivosInfo = new ActualizarActivosInfoUseCase();
    }

    @Override
    public void run() {
        if(!Bukkit.getServer().getOnlinePlayers().isEmpty() || this.actualizarActivosInfo.isLoading()){
            this.actualizarActivosInfo.actualizar();
        }
    }
}
