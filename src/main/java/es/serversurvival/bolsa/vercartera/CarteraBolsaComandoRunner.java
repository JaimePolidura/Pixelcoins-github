package es.serversurvival.bolsa.vercartera;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "bolsa cartera", explanation = "Ver todas las posiciones que tienes")
public class CarteraBolsaComandoRunner implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender sender) {
        BolsaCarteraMenu menu = new BolsaCarteraMenu((Player) sender);
    }
}
