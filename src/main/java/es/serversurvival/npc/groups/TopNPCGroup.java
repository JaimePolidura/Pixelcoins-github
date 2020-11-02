package es.serversurvival.npc.groups;

import es.serversurvival.npc.NPCCreator;
import es.serversurvival.util.Funciones;
import net.jitse.npclib.api.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

public class TopNPCGroup extends NPCGroup{
    private final int maxNumber = 3;
    private Map<Integer ,Location> locationMap;

    public TopNPCGroup () {
        locationMap = new HashMap<>();

        World world = Bukkit.getWorld("survival");

        locationMap.put(1, new Location(world, 271.5, 66, -207.5, 90, 0));
        locationMap.put(2, new Location(world, 271.5, 65, -208.5, 90, 0));
        locationMap.put(3, new Location(world, 271.5, 64, -206.5, 90, 0));
    }

    @Override
    public int getMaxNumberNPCs() {
        return maxNumber;
    }

    @Override
    public void updateAll() {
        clearNPCs();

        Map<String, Double> topJugadores = Funciones.crearMapaTopPatrimonioPlayers(false);
        int top = 1;

        for(Map.Entry<String, Double> entry : topJugadores.entrySet()){
            if(top >= 4) break;

            String nombreJugador = entry.getKey();
            double patrimonioJugador = entry.getValue();
            Location location = locationMap.get(top);

            NPC npc = NPCCreator.createTopNPC(nombreJugador, location, entry.getValue(), top);

            addNPC(npc);

            top++;
        }

        showAll();
    }
}
