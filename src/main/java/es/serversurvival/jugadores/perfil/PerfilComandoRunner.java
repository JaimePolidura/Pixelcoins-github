package es.serversurvival.jugadores.perfil;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "perfil", explanation = "Ver tus estadisticas")
public class PerfilComandoRunner extends PixelcoinCommand implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender sender) {
        PerfilMenu perfilMenu = new PerfilMenu((Player) sender);
    }
}
