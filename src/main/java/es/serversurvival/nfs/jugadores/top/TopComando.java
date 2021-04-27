package es.serversurvival.nfs.jugadores.top;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.nfs.shared.comandos.PixelcoinCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@Command("top")
public class TopComando extends PixelcoinCommand implements CommandRunner {
    public final static String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "              TOP";

    @Override
    public void execute(CommandSender player, String[] args) {
        TopMenu topMenu = new TopMenu((Player) player);
    }
}
