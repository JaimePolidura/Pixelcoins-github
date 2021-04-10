package es.serversurvival.task;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.npc.NPCManager;
import org.bukkit.scheduler.BukkitRunnable;

@Task(period = 5 * BukkitTimeUnit.MINUTE, delay = 15 * BukkitTimeUnit.SECOND)
public class ActualizarNPCs implements TaskRunner {
    @Override
    public void run() {
        NPCManager.updateAllGroups();
    }
}
