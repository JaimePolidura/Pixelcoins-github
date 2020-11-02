package es.serversurvival.comandos.subComandos.deudas;

import es.serversurvival.comandos.CommandManager;
import es.serversurvival.comandos.SubComando;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;

public class AyudaDeudas extends DeudasSubCommand {
    private final String scnombre = "ayuda";
    private final String sintaxis = "/deudas ayuda";
    private final String ayuda = "ver todas los subcomandos relacionados con el comando deudas";

    public String getSCNombre() {
        return scnombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        Set<SubComando> subComandoSet = CommandManager.getSubComandos();

        player.sendMessage("        ");
        player.sendMessage(ChatColor.GOLD + "Todos los subcomandos del comando deuda:  Mas info en /ayuda deudas");
        for (SubComando subComando : subComandoSet) {
            if (subComando instanceof DeudasSubCommand && subComando != this) {
                player.sendMessage("   ");
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + subComando.getSintaxis() + ChatColor.RESET + "" + ChatColor.GOLD + ": " + subComando.getAyuda());
            }
        }
    }
}
