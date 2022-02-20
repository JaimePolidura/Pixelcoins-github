package es.serversurvival.empresas.vertodas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerArgs;
import es.jaimetruman.commands.CommandRunnerNonArgs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("empresas vertodas")
public class VerTodasEmpresasComandoExecutor implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender sender) {
        EmpresasVerTodasMenu menu = new EmpresasVerTodasMenu((Player) sender);
    }
}
