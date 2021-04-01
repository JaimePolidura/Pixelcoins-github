package es.serversurvival.comandos.subComandos.empresas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.PixelcoinCommand;
import es.serversurvival.mySQL.Empresas;
import es.serversurvival.mySQL.MySQL;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(name = "empresas editarnombre")
public class EditarNombreEmpresas extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas editarnombre <empresa> <nuevo nombre>";

    @Override
    public void execute(CommandSender player, String[] args) {
        MySQL.conectar();

        ValidationResult result = ValidationsService.startValidating(args.length == 3, True.of(usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, OwnerDeEmpresa.of(player.getName()), MaxLength.of(Empresas.CrearEmpresaNombreLonMax, "El nombre no puede ser tan largo"))
                .andMayThrowException(() -> args[2], usoIncorrecto, NombreEmpresaNoPillado)
                .validateAll();

        if(result.isFailed())
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
        else
            empresasMySQL.cambiarNombre((Player) player, args[1], args[2]);

        MySQL.desconectar();
    }
}
