package es.serversurvival.bolsa.activosinfo.actualizar;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

@Task(value = 3 * BukkitTimeUnit.MINUTE, delay = BukkitTimeUnit.MINUTE)
@AllArgsConstructor
public class ActualizarActivosInfoTask implements TaskRunner {
    private final ActualizarActivosInfoUseCase actualizarActivosInfo;

    @Override
    public void run() {
        if(!Bukkit.getServer().getOnlinePlayers().isEmpty() || this.actualizarActivosInfo.isLoading()){
            this.actualizarActivosInfo.actualizar();
        }
    }
}
