package es.serversurvival.comandos.subComandos.empleado;

import es.serversurvival.comandos.CommandManager;
import es.serversurvival.comandos.SubComando;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;

public class AyudaEmpleos extends EmpleosSubCommand {
    private final String SCNombre = "ayuda";
    private final String sintaxis = "/empleos ayuda";
    private final String ayuda = "ver toda los comandos relacionados con el trabajo";

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
        player.sendMessage(ChatColor.GOLD + "Todos los subcomandos del comando empleados:  Mas info en /ayuda empleo");
        for (SubComando subComando : subComandoSet) {
            if (subComando instanceof EmpleosSubCommand && subComando != this) {
                player.sendMessage("   ");
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + subComando.getSintaxis() + ChatColor.RESET + "" + ChatColor.GOLD + ": " + subComando.getAyuda());
            }
        }
    }
}
