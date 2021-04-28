package es.serversurvival.empresas.miempresa;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.shared.comandos.PixelcoinCommand;
import es.serversurvival.utils.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.DARK_RED;

@Command("empresas miempresa")
public class MiEmpresaComando extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas miempresa <empresa>";

    @Override
    public void execute(CommandSender player, String[] args) {
        ValidationResult result = ValidationsService.startValidating(args.length == 2, Validaciones.True.of(usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, Validaciones.OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()) {
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        EmpresasVerMenu menu = new EmpresasVerMenu((Player) player, args[1]);
    }
}
