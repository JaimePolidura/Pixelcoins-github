package es.serversurvival.bolsa.verordenespremarket;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerNonArgs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "bolsa ordenes", isSubCommand = true)
public class OrdenesBolsa implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender sender) {
        BolsaOrdenesMenu menu = new BolsaOrdenesMenu((Player) sender);
    }
}
