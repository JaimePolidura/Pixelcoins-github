package es.serversurvival.legacy.comandos.subComandos.bolsa;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.comandos.PixelcoinCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "bolsa per")
public class PerBolsa extends PixelcoinCommand implements CommandRunner {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length != 2){
            sender.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /bolsa per <ticker>");
            return;
        }
        String ticker = args[1];
        llamadasApiMySQL.mostrarRatioPer((Player) sender, ticker);
    }
}
