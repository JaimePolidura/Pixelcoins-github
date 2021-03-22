package es.serversurvival.npc;

import es.serversurvival.apiHttp.MojangAPI;
import es.serversurvival.main.Pixelcoin;
import es.serversurvival.util.Funciones;
import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.skin.Skin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.json.simple.JSONObject;

import java.text.DecimalFormat;
import java.util.Arrays;


public final class NPCCreator {
    private static NPCLib libray = new NPCLib(Pixelcoin.getInstance());
    private static DecimalFormat formatea = Funciones.FORMATEA;

    private NPCCreator () {}

    public static NPC createTopNPC (String nombreJugador, Location location, double patrimonio, int topRicos) {
        Skin skin = null;

        try {
            JSONObject skinJson = MojangAPI.getSkinInfo(Bukkit.getOfflinePlayer(nombreJugador).getUniqueId().toString());
            String value = (String) skinJson.get("value");
            String signature = (String) skinJson.get("signature");

            skin = new Skin(value, signature);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return libray.createNPC()
                .setLocation(location)
                .setSkin(skin)
                .setText(Arrays.asList(ChatColor.GOLD + nombreJugador, ChatColor.GREEN + formatea.format(patrimonio)  + " PC"))
                .create();
    }
}