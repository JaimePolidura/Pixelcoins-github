package es.serversurvival.empresas.empresas.editarempleado;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas editarempleado",
        args = {"empresa"},
        explanation = "Editar a un empleado"
)
public class EditarEmpleadoComandoExecutor implements CommandRunnerArgs<EditarEmpleadoComando> {
    @Override
    public void execute(EditarEmpleadoComando comando, CommandSender sender) {
        if(comando.getEmpresa().equalsIgnoreCase(""))
            sender.sendMessage(ChatColor.GOLD + "Para editar empleados: http://serversurvival.ddns.net/profile/empresas/info/" + comando.getEmpresa() +AQUA+" /cuenta" +GOLD+ "para registrarse");
        else
            sender.sendMessage(ChatColor.GOLD + "Para editar empleados: http://serversurvival.ddns.net/profile/empresas "+AQUA+" /cuenta" +GOLD+ "para registrarse");

    }
}
