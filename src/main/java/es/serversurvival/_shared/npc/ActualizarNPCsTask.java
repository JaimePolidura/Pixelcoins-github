package es.serversurvival._shared.npc;

import es.jaimetruman.annotations.Component;
import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;

@Task(value = 5 * BukkitTimeUnit.MINUTE, delay = 15 * BukkitTimeUnit.SECOND)
@Component
public class ActualizarNPCsTask implements TaskRunner {
    @Override
    public void run() {
        NPCManager.updateAllGroups();
    }
}
