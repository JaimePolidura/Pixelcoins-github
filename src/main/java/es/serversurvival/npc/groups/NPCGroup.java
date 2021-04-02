package es.serversurvival.npc.groups;

import net.jitse.npclib.api.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;


public abstract class NPCGroup {
    private final List<NPC> npcs = new ArrayList<>();

    public abstract void updateAll();

    public void showAll () {
        Bukkit.getOnlinePlayers().forEach( (player) -> {
            npcs.forEach((npc) -> npc.show(player));
        });
    }

    public void showPlayer (Player player) {
        npcs.forEach( npc -> npc.show(player) );
    }

    public void clearNPCs () {
        npcs.forEach(NPC::destroy);
        npcs.clear();
    }

    public void addNPC (NPC npc) {
        npcs.add(npc);
    }
}
