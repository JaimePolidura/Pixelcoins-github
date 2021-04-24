package es.serversurvival.legacy.comandos.subComandos.empresas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.comandos.PixelcoinCommand;
import es.serversurvival.legacy.menus.menus.EmpresasMercadoMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "empresas mercado")
public class MercadoEmpresas extends PixelcoinCommand implements CommandRunner {
    @Override
    public void execute(CommandSender sender, String[] args) {
        EmpresasMercadoMenu empresasMercadoMenu = new EmpresasMercadoMenu((Player) sender);
    }
}
