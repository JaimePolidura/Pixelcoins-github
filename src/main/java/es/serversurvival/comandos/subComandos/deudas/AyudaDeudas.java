package es.serversurvival.comandos.subComandos.deudas;

import es.serversurvival.comandos.CommandManager;
import es.serversurvival.comandos.SubComando;
import es.serversurvival.main.Pixelcoin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
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
        Map<String, SubComando> subComandosDeudas = Pixelcoin.getCommandManager().getSubComandosDe(getCNombre());

        player.sendMessage("        ");
        player.sendMessage(ChatColor.GOLD + "Todos los subcomandos del comando deuda:  Mas info en /ayuda deudas");
        for(Map.Entry<String, SubComando> entry : subComandosDeudas.entrySet()){
            SubComando subComando = entry.getValue();

            if(subComando != this){
                player.sendMessage("   ");
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + subComando.getSintaxis() + ChatColor.RESET + "" + ChatColor.GOLD + ": " + subComando.getAyuda());
            }
        }
    }
}
