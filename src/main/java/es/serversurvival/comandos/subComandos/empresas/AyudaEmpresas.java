package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.comandos.CommandManager;
import es.serversurvival.comandos.SubComando;
import es.serversurvival.main.Pixelcoin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
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
        Map<String, SubComando> subComandosEMpresas = Pixelcoin.getCommandManager().getSubComandosDe(getCNombre());

        player.sendMessage("        ");
        player.sendMessage(ChatColor.GOLD + "Todos los subcomandos de las empresas:  Mas info en /ayuda empresario");
        for(Map.Entry<String, SubComando> entry : subComandosEMpresas.entrySet()) {
            SubComando subComando = entry.getValue();

            if (subComando != this) {
                player.sendMessage("    ");
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + subComando.getSintaxis() + ChatColor.RESET + "" + ChatColor.GOLD + ": " + subComando.getAyuda());
            }
        }
    }
}
