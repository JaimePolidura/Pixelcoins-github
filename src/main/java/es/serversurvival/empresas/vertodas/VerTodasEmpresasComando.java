package es.serversurvival.empresas.vertodas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("empresas vertodas")
public class VerTodasEmpresasComando implements CommandRunner {
    @Override
    public void execute(CommandSender sender, String[] args) {
        EmpresasVerTodasMenu menu = new EmpresasVerTodasMenu((Player) sender);
    }
}
