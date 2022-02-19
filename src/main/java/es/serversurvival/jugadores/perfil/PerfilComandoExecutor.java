package es.serversurvival.jugadores.perfil;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerNonArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("perfil")
public class PerfilComandoExecutor extends PixelcoinCommand implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender sender) {
        PerfilMenu perfilMenu = new PerfilMenu((Player) sender);
    }
}
