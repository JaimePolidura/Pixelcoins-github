package es.serversurvival.legacy.comandos.subComandos.empleado;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.comandos.PixelcoinCommand;
import es.serversurvival.legacy.menus.menus.EmpleosMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "empleos misempleos")
public class MisTrabajosEmpleos extends PixelcoinCommand implements CommandRunner {
    @Override
    public void execute(CommandSender sender, String[] args) {
        EmpleosMenu menu = new EmpleosMenu((Player) sender);
    }
}
