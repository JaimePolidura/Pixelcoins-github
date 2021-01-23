package es.serversurvival.comandos.subComandos.empleado;

import es.serversurvival.comandos.CommandManager;
import es.serversurvival.comandos.SubComando;
import es.serversurvival.main.Pixelcoin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
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
        Map<String, SubComando> subComandosEmleos = Pixelcoin.getCommandManager().getSubComandosDe(getCNombre());

        player.sendMessage("        ");
        player.sendMessage(ChatColor.GOLD + "Todos los subcomandos del comando empleados:  Mas info en /ayuda empleo");

        for(Map.Entry<String, SubComando> entry : subComandosEmleos.entrySet()){
            SubComando subComando = entry.getValue();

            if(subComando != this){
                player.sendMessage("   ");
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + subComando.getSintaxis() + ChatColor.RESET + "" + ChatColor.GOLD + ": " + subComando.getAyuda());
            }
        }
    }
}
