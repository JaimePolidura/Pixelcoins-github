package es.serversurvival.empleados.misempleos;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerArgs;
import es.jaimetruman.commands.CommandRunnerNonArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("empleos misempleos")
public class MisEmpleosComandoExecutor extends PixelcoinCommand implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender sender) {
        EmpleosMenu menu = new EmpleosMenu((Player) sender);
    }
}
