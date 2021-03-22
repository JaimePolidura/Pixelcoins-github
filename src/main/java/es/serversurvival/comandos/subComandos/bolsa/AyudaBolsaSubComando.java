package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.comandos.CommandManager;
import es.serversurvival.comandos.SubComando;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;

public class AyudaBolsaSubComando extends BolsaSubCommand {
    private final String SCNOmbre = "ayuda";
    private final String sintaxis = "/bolsa ayuda";
    private final String ayuda = "ayuda de los subcomandos de la bolsa";

    public String getSCNombre() {
        return SCNOmbre;
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
        player.sendMessage(ChatColor.GOLD + "Todos los subcomandos del comando bolsa:  Mas info en /ayuda bolsa");
        for (SubComando subComando : subComandoSet) {
            if (subComando instanceof BolsaSubCommand && subComando != this) {
                player.sendMessage("   ");
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + subComando.getSintaxis() + ChatColor.RESET + "" + ChatColor.GOLD + ": " + subComando.getAyuda());
            }
        }
    }
}
