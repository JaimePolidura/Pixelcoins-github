package es.serversurvival.bolsa.other.verofertasmercadoserver;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "empresas mercado",
        explanation = "Ver todas las ofertas de venta de acciones de empresas del servidor"
)
public class MercadoEmpresas extends PixelcoinCommand implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender sender) {
        EmpresasMercadoMenu empresasMercadoMenu = new EmpresasMercadoMenu((Player) sender);
    }
}
