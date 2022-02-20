package es.serversurvival.deudas.ver;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerNonArgs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("deudas ver")
public class VerDeudasComandoExecutor implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender sender) {
        DeudasMenu menu = new DeudasMenu((Player) sender);
    }
}
