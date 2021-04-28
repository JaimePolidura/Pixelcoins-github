package es.serversurvival.jugadores.perfil;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.shared.comandos.PixelcoinCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("perfil")
public class PerfilComando extends PixelcoinCommand implements CommandRunner {

    @Override
    public void execute(CommandSender sender, String[] args) {
        PerfilMenu perfilMenu = new PerfilMenu((Player) sender);
    }
}
