package es.serversurvival.nfs.empresas.editardescripccion;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.comandos.PixelcoinCommand;
import es.serversurvival.nfs.empresas.mysql.Empresas;
import es.serversurvival.nfs.utils.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import static es.serversurvival.nfs.utils.Funciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command("empresas editardescripccion")
public class EditarDescComando extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas editardescripccion <empresa> <nueva desc>";
    private EditarDescUseCase useCase = EditarDescUseCase.INSTANCE;

    @Override
    public void execute(CommandSender player, String[] args) {
        ValidationResult result = ValidationsService.startValidating(args.length >= 3, Validaciones.True.of(usoIncorrecto))
                .andMayThrowException(() -> buildStringFromArray(args, 2), usoIncorrecto, Validaciones.MaxLength.of(Empresas.CrearEmpresaDescLonMax, "La descripccino no puede ser tan larga"))
                .andMayThrowException(() -> args[1], usoIncorrecto, Validaciones.OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if (result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        String nombreEmpresa = args[1];
        String descripccion = buildStringFromArray(args, 2);

        useCase.edit(nombreEmpresa, descripccion);

        player.sendMessage(ChatColor.GOLD + "Has cambiado la descripccion de tu empresa: " + ChatColor.DARK_AQUA + nombreEmpresa +
                ChatColor.GOLD + " a ver en " + ChatColor.AQUA + "/empresas vertodas");
    }
}
