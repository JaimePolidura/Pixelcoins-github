package es.serversurvival.task;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MensajeWeb extends BukkitRunnable {
    public static final int delay = 1200;
    private Server server;
    private final String URL = "http://www.serversurvival2.ddns.net";

    public MensajeWeb(Server s) {
        server = s;
    }

    @Override
    public void run() {
        for (Player p : server.getOnlinePlayers()) {
            p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Web del server: " + ChatColor.AQUA + "" + ChatColor.UNDERLINE + "" + URL);
        }
    }
}