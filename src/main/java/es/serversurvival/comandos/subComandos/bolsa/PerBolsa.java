package es.serversurvival.comandos.subComandos.bolsa;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PerBolsa extends BolsaSubCommand{
    private final String SCNombre = "per";
    private final String sintaxis = "/bolsa per <ticker>";
    private final String ayuda = "Ver el ratio del PER mediante el ticker de la accion";

    @Override
    public String getSCNombre() {
        return SCNombre;
    }

    @Override
    public String getSintaxis() {
        return sintaxis;
    }

    @Override
    public String getAyuda() {
        return ayuda;
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args.length != 2){
            player.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + sintaxis);
            return;
        }
        String ticker = args[1];
        llamadasApiMySQL.mostrarRatioPer(player, ticker);
    }
}