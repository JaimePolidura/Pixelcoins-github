package es.serversurvival.npc.groups;


import net.jitse.npclib.api.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;


public abstract class NPCGroup {

    private List<NPC> npcs = new ArrayList<>();

    public abstract int getMaxNumberNPCs();
    public abstract void updateAll();

    public void showAll () {
        Bukkit.getOnlinePlayers().forEach( (player) -> {
            npcs.forEach((npc) -> npc.show(player));
        });
    }

    public void showPlayer (Player player) {
        npcs.forEach( npc -> npc.show(player) );
    }

    public boolean isEmpty () {
        return npcs.size() == 0;
    }

    public void clearNPCs () {
        npcs.forEach(NPC::destroy);
        npcs.clear();
    }

    public void addNPC (NPC npc) {
        if(npcs.size() == getMaxNumberNPCs())
            throw new IndexOutOfBoundsException("Se ha llegado al limite de NPCs por grupo, debes limpiar los npcs antes de poner nuevos");

        npcs.add(npc);
    }
}