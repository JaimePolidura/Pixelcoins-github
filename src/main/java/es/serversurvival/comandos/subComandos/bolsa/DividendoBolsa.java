package es.serversurvival.comandos.subComandos.bolsa;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.PixelcoinCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "bolsa dividendo")
public class DividendoBolsa extends PixelcoinCommand implements CommandRunner {
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
