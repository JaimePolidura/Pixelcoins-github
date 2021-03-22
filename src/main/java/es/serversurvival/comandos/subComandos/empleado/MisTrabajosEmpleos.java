package es.serversurvival.comandos.subComandos.empleado;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.ComandoUtilidades;
import es.serversurvival.menus.menus.EmpleosMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "empleos misempleos")
public class MisTrabajosEmpleos extends ComandoUtilidades implements CommandRunner {
    @Override
    public void execute(CommandSender sender, String[] args) {
        EmpleosMenu menu = new EmpleosMenu((Player) sender);
    }
}
