package es.serversurvival.npc;

import es.serversurvival.npc.groups.NPCGroup;
import es.serversurvival.npc.groups.TopNPCGroup;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public final class NPCManager {
    private static Set<NPCGroup> npcGroups = new HashSet<>();

    private NPCManager () {}

    static  {
        npcGroups.add(new TopNPCGroup());
    }

    public static void addGroup (NPCGroup group) {
        npcGroups.add(group);
    }

    public static void updateAllGroups () {
        Set<NPCGroup> copyOfNpcGroups = new HashSet<>(npcGroups);

        copyOfNpcGroups.forEach(NPCGroup::updateAll);
    }

    public static void showPlayer (Player player) {
        npcGroups.forEach( group -> group.showPlayer(player) );
    }
}