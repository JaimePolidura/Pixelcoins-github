package es.serversurvival.deudas.ver;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("deudas ver")
public class VerDeudas implements CommandRunner {
    @Override
    public void execute(CommandSender sender, String[] args) {
        DeudasMenu menu = new DeudasMenu((Player) sender);
    }
}
