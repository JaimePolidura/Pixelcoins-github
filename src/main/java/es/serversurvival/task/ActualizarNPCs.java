package es.serversurvival.task;

import es.serversurvival.npc.NPCManager;
import org.bukkit.scheduler.BukkitRunnable;

public class ActualizarNPCs extends BukkitRunnable {

    @Override
    public void run() {
        NPCManager.updateAllGroups();
    }
}