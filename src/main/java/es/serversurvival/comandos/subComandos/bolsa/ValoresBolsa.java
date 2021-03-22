package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.objetos.menus.ElegirInversionMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ValoresBolsa extends BolsaSubCommand {
    private final String SCNombre = "valores";
    private final String sintaxis = "/bolsa valores";
    private final String ayuda = "ver una lista de valores de bolsa de Estados Unidos";
    public static final String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "    /ayuda bolsa";

    public String getSCNombre() {
        return SCNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        ElegirInversionMenu elegirInversionMenu = new ElegirInversionMenu(player);
        elegirInversionMenu.openMenu();
    }
}