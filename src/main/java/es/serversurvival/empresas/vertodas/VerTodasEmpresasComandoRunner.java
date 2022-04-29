package es.serversurvival.empresas.vertodas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "empresas vertodas",
        explanation = "Ver los datos de todas las empresas del servidor"
)
public class VerTodasEmpresasComandoRunner implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender sender) {
        EmpresasVerTodasMenu menu = new EmpresasVerTodasMenu((Player) sender);
    }

    public static void main(String[] args) {
        System.out.println("HOla?");
    }
}
