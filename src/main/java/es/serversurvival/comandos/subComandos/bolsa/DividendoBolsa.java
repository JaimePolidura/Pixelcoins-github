package es.serversurvival.comandos.subComandos.bolsa;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.ComandoUtilidades;
import es.serversurvival.mySQL.MySQL;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;

@Command(name = "bolsa dividendo")
public class DividendoBolsa extends ComandoUtilidades implements CommandRunner {
    @Override
    public void execute(CommandSender player, String[] args) {
        if(args.length > 2){
            player.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /bolsa dividendo [ticker]");
            return;
        }

        boolean verCarteraEntera = args.length == 1;

        if(verCarteraEntera){
            posicionesAbiertasMySQL.mostrarDividendosCarteraEntera((Player) player);
        }else{
            posicionesAbiertasMySQL.mostrarDividendoEmpresa((Player) player, args[1]);
        }
    }
}
