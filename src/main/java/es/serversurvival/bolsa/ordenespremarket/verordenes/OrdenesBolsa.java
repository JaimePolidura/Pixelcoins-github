package es.serversurvival.bolsa.ordenespremarket.verordenes;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("bolsa ordenes")
public class OrdenesBolsa implements CommandRunner {
    @Override
    public void execute(CommandSender player, String[] args) {
        BolsaOrdenesMenu menu = new BolsaOrdenesMenu((Player) player);
    }
}
