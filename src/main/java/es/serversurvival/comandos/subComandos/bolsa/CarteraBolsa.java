package es.serversurvival.comandos.subComandos.bolsa;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.menus.menus.BolsaCarteraMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "bolsa cartera")
public class
CarteraBolsa implements CommandRunner {
    @Override
    public void execute(CommandSender player, String[] args) {
        BolsaCarteraMenu menu = new BolsaCarteraMenu((Player) player);
    }
}
