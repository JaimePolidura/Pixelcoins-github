package es.serversurvival.bolsa.vercartera;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerNonArgs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("bolsa cartera")
public class CarteraBolsaComandoExecutor implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender sender) {
        BolsaCarteraMenu menu = new BolsaCarteraMenu((Player) sender);
    }
}
