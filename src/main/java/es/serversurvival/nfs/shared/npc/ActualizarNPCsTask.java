package es.serversurvival.nfs.shared.npc;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.nfs.shared.npc.NPCManager;

@Task(value = 5 * BukkitTimeUnit.MINUTE, delay = 15 * BukkitTimeUnit.SECOND)
public class ActualizarNPCsTask implements TaskRunner {
    @Override
    public void run() {
        NPCManager.updateAllGroups();
    }
}
