package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.menus.menus.TopMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class Top extends Comando {
    private final String cnombre = "top";
    private final String sintaxis = "/top";
    private final String ayuda = "ver los jugadores top";
    public final static String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "              TOP";

    public String getCNombre() {
        return cnombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        TopMenu topMenu = new TopMenu(player);
        topMenu.openMenu();
    }
}
