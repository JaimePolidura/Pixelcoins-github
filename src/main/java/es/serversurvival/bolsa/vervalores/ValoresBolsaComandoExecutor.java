package es.serversurvival.bolsa.vervalores;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerNonArgs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("bolsa valores")
public class ValoresBolsaComandoExecutor implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender sender) {
        ElegirInversionMenu menu = new ElegirInversionMenu((Player) sender);
    }
}
