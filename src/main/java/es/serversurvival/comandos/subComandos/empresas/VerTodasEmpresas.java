package es.serversurvival.comandos.subComandos.empresas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.menus.menus.EmpresasVerTodasMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "empresas vertodas")
public class VerTodasEmpresas implements CommandRunner {
    @Override
    public void execute(CommandSender sender, String[] args) {
        EmpresasVerTodasMenu menu = new EmpresasVerTodasMenu((Player) sender);
    }
}
