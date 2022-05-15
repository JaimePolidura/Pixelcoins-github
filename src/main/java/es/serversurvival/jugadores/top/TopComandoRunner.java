package es.serversurvival.jugadores.top;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@Command(
        value = "top",
        explanation = "Ver el top ricos, pobres, mejores vendedores etc"
)
public class TopComandoRunner implements CommandRunnerNonArgs {
    public final static String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "              TOP";

    @Override
    public void execute(CommandSender player) {
        TopMenu topMenu = new TopMenu((Player) player);
    }
}
