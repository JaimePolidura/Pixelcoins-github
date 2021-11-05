package es.serversurvival.bolsa.vervalores;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("bolsa valores")
public class ValoresBolsaComando implements CommandRunner {
    @Override
    public void execute(CommandSender sender, String[] args) {
        ElegirInversionMenu menu = new ElegirInversionMenu((Player) sender);
    }
}
