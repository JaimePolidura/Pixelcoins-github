package es.serversurvival.bolsa.verofertasmercadoserver;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerNonArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "empresas mercado", isSubCommand = true)
public class MercadoEmpresas extends PixelcoinCommand implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender sender) {
        EmpresasMercadoMenu empresasMercadoMenu = new EmpresasMercadoMenu((Player) sender);
    }
}
