package es.serversurvival.bolsa.vervalores;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "bolsa valores", explanation = "Ver un ejemplo de valores que puedes invertir")
public class ValoresBolsaComandoRunner implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender sender) {
        ElegirInversionMenu menu = new ElegirInversionMenu((Player) sender);
    }
}
