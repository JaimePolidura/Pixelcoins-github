package es.serversurvival.legacy.comandos.subComandos.bolsa;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.menus.menus.ElegirInversionMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "bolsa valores")
public class ValoresBolsa implements CommandRunner {
    @Override
    public void execute(CommandSender sender, String[] args) {
        ElegirInversionMenu menu = new ElegirInversionMenu((Player) sender);
    }
}
