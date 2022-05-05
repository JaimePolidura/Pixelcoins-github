package es.serversurvival.deudas.ver;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "deudas ver",
        explanation = "Ver todas las deudas que tengas y que tengan contigo"
)
public class VerDeudasComandoExecutor implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender sender) {
        DeudasMenu menu = new DeudasMenu((Player) sender);
    }
}
