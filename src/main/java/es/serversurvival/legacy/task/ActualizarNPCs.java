package es.serversurvival.legacy.task;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.legacy.npc.NPCManager;

@Task(period = 5 * BukkitTimeUnit.MINUTE, delay = 15 * BukkitTimeUnit.SECOND)
public class ActualizarNPCs implements TaskRunner {
    @Override
    public void run() {
        NPCManager.updateAllGroups();
    }
}
