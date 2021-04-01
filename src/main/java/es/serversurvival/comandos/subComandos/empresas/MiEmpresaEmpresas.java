package es.serversurvival.comandos.subComandos.empresas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.PixelcoinCommand;
import es.serversurvival.menus.menus.EmpresasVerMenu;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(name = "empresas miempresa")
public class MiEmpresaEmpresas extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas miempresa <empresa>";

    @Override
    public void execute(CommandSender player, String[] args) {

        ValidationResult result = ValidationsService.startValidating(args.length == 2, True.of(usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()) {
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
        }else{
            EmpresasVerMenu menu = new EmpresasVerMenu((Player) player, args[1]);
        }

    }
}
