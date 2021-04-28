package es.serversurvival.empleados.misempleos;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.shared.comandos.PixelcoinCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("empleos misempleos")
public class MisEmpleosComando extends PixelcoinCommand implements CommandRunner {
    @Override
    public void execute(CommandSender sender, String[] args) {
        EmpleosMenu menu = new EmpleosMenu((Player) sender);
    }
}
