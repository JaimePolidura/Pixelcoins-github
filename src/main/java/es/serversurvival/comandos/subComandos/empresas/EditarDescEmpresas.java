package es.serversurvival.comandos.subComandos.empresas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.PixelcoinCommand;
import es.serversurvival.mySQL.Empresas;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(name = "empresas editardescripccion")
public class EditarDescEmpresas extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas editardescripccion <empresa> <nueva desc>";

    @Override
    public void execute(CommandSender player, String[] args) {
        MySQL.conectar();

        ValidationResult result = ValidationsService.startValidating(args.length >= 3, True.of(usoIncorrecto))
                .andMayThrowException(() -> Funciones.buildStringFromArray(args, 2), usoIncorrecto, MaxLength.of(Empresas.CrearEmpresaDescLonMax, "La descripccino no puede ser tan larga"))
                .andMayThrowException(() -> args[1], usoIncorrecto, OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed())
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
        else
            empresasMySQL.cambiarDescripciom(args[1], Funciones.buildStringFromArray(args, 2), (Player) player);

        MySQL.desconectar();
    }
}
