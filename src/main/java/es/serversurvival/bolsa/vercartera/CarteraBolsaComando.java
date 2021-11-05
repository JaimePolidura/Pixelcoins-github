package es.serversurvival.bolsa.vercartera;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("bolsa cartera")
public class CarteraBolsaComando implements CommandRunner {
    @Override
    public void execute(CommandSender player, String[] args) {
        BolsaCarteraMenu menu = new BolsaCarteraMenu((Player) player);
    }
}
