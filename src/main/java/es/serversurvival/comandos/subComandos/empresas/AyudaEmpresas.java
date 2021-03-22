package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.comandos.CommandManager;
import es.serversurvival.comandos.SubComando;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;

public class AyudaEmpresas extends EmpresasSubCommand {
    private final String SCNombre = "ayuda";
    private final String sintaxis = "/empresas ayuda";
    private final String ayuda = "mostrar ayuda empresas";

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
        Set<SubComando> subComandoSet = CommandManager.getSubComandos();

        player.sendMessage("        ");
        player.sendMessage(ChatColor.GOLD + "Todos los subcomandos de las empresas:  Mas info en /ayuda empresario");
        for (SubComando subComando : subComandoSet) {
            if (subComando instanceof EmpresasSubCommand && subComando != this) {
                player.sendMessage("    ");
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + subComando.getSintaxis() + ChatColor.RESET + "" + ChatColor.GOLD + ": " + subComando.getAyuda());
            }
        }
    }
}
