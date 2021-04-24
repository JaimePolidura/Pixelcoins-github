package es.serversurvival.legacy.npc;

import es.serversurvival.legacy.npc.groups.NPCGroup;
import es.serversurvival.legacy.npc.groups.TopNPCGroup;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public final class NPCManager {
    private static Set<NPCGroup> npcGroups = new HashSet<>();

    private NPCManager () {}

    static  {
        npcGroups.add(new TopNPCGroup());
    }

    public static void updateAllGroups () {
        Set<NPCGroup> copyOfNpcGroups = new HashSet<>(npcGroups);

        copyOfNpcGroups.forEach(NPCGroup::updateAll);
    }

    public static void showPlayer (Player player) {
        npcGroups.forEach( group -> group.showPlayer(player) );
    }
}
