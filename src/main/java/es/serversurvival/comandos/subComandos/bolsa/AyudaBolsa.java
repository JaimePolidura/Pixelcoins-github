package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.comandos.CommandManager;
import es.serversurvival.comandos.SubComando;
import es.serversurvival.main.Pixelcoin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;

public class AyudaBolsa extends BolsaSubCommand {
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
        Map<String, SubComando> subComandosBolsa = Pixelcoin.getCommandManager().getSubComandosDe(getCNombre());

        player.sendMessage("        ");
        player.sendMessage(ChatColor.GOLD + "Todos los subcomandos del comando bolsa:  Mas info en /ayuda bolsa");
        for(Map.Entry<String, SubComando> entry : subComandosBolsa.entrySet()){
            SubComando subComando = entry.getValue();

            if(subComando != this){
                player.sendMessage("   ");
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + subComando.getSintaxis() + ChatColor.RESET + "" + ChatColor.GOLD + ": " + subComando.getAyuda());
            }
        }
    }
}
