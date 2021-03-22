package es.serversurvival.comandos.subComandos.ayuda;

import es.serversurvival.comandos.SubComando;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class AyudaSubCommand extends SubComando {
    private String CNombre = "ayuda";

    public String getCNombre() {
        return CNombre;
    }

    public static void mostrarAyudas(Player p) {
        p.sendMessage(ChatColor.YELLOW + "Puedes elegir entre:");
        p.sendMessage("         ");
        p.sendMessage(ChatColor.YELLOW + "/ayuda empleo");
        p.sendMessage(ChatColor.YELLOW + "/ayuda empresario");
        p.sendMessage(ChatColor.YELLOW + "/ayuda deuda");
        p.sendMessage(ChatColor.YELLOW + "/ayuda pixelcoins");
        p.sendMessage(ChatColor.YELLOW + "/ayuda tienda");
        p.sendMessage(ChatColor.YELLOW + "/ayuda normas");
        p.sendMessage(ChatColor.YELLOW + "/ayuda jugar");
    }
}