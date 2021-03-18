package es.serversurvival.comandos.subComandos.deudas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.menus.menus.DeudasMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "deudas ver")
public class VerDeudas implements CommandRunner {
    @Override
    public void execute(CommandSender sender, String[] args) {
        DeudasMenu menu = new DeudasMenu((Player) sender);
    }
}
