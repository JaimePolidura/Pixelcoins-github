package es.serversurvival.empleados.misempleos;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "empleos misempleos", explanation = "Ver todos los empleos en los que estes contratado")
public class MisEmpleosComandoRunner extends PixelcoinCommand implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender sender) {
        EmpleosMenu menu = new EmpleosMenu((Player) sender);
    }
}
