package es.serversurvival.jugadores.top;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerNonArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@Command("top")
public class TopComandoExecutor extends PixelcoinCommand implements CommandRunnerNonArgs {
    public final static String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "              TOP";

    @Override
    public void execute(CommandSender player) {
        TopMenu topMenu = new TopMenu((Player) player);
    }
}
