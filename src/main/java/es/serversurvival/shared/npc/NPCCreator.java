package es.serversurvival.shared.npc;

import es.serversurvival.Pixelcoin;
import es.serversurvival.shared.utils.apiHttp.MojangAPI;
import es.serversurvival.shared.utils.Funciones;
import lombok.SneakyThrows;
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
    private static final NPCLib libray = new NPCLib(Pixelcoin.getInstance());
    private static final DecimalFormat formatea = Funciones.FORMATEA;

    private NPCCreator () {}

    @SneakyThrows
    public static NPC createTopNPC (String nombreJugador, Location location, double patrimonio, int topRicos) {
        JSONObject skinJson = MojangAPI.getSkinInfo(Bukkit.getOfflinePlayer(nombreJugador).getUniqueId().toString());
        String value = (String) skinJson.get("value");
        String signature = (String) skinJson.get("signature");
        Skin skin = new Skin(value, signature);

        return libray.createNPC()
                .setLocation(location)
                .setSkin(skin)
                .setText(Arrays.asList(ChatColor.GOLD + nombreJugador, ChatColor.GREEN + formatea.format(patrimonio)  + " PC"))
                .create();
    }
}
