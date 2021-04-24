package es.serversurvival.legacy.comandos.subComandos.bolsa;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.menus.menus.BolsaOrdenesMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "bolsa ordenes")
public class OrdenesBolsa implements CommandRunner {
    @Override
    public void execute(CommandSender player, String[] args) {
        BolsaOrdenesMenu menu = new BolsaOrdenesMenu((Player) player);
    }
}
