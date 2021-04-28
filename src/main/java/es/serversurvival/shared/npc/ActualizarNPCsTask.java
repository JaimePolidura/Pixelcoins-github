package es.serversurvival.shared.npc;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;

@Task(value = 5 * BukkitTimeUnit.MINUTE, delay = 15 * BukkitTimeUnit.SECOND)
public class ActualizarNPCsTask implements TaskRunner {
    @Override
    public void run() {
        NPCManager.updateAllGroups();
    }
}
