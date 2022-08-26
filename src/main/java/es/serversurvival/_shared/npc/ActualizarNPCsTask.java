package es.serversurvival._shared.npc;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import es.jaimetruman.annotations.Component;

@Task(value = 5 * BukkitTimeUnit.MINUTE, delay = 15 * BukkitTimeUnit.SECOND)
public class ActualizarNPCsTask implements TaskRunner {
    @Override
    public void run() {
        NPCManager.updateAllGroups();
    }
}
