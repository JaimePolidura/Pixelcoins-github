package es.serversurvival.jugadores.perfil;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "perfil", explanation = "Ver tus estadisticas")
public class PerfilComandoRunner implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender sender) {
        PerfilMenu perfilMenu = new PerfilMenu((Player) sender);
    }
}
