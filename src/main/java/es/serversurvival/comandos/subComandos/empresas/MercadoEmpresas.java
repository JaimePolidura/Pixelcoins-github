package es.serversurvival.comandos.subComandos.empresas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.ComandoUtilidades;
import es.serversurvival.menus.menus.EmpresasMercadoMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "empresas mercado")
public class MercadoEmpresas extends ComandoUtilidades implements CommandRunner {
    @Override
    public void execute(CommandSender sender, String[] args) {
        EmpresasMercadoMenu empresasMercadoMenu = new EmpresasMercadoMenu((Player) sender);
    }
}
