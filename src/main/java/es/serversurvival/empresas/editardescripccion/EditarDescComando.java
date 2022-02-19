package es.serversurvival.empresas.editardescripccion;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.empresas._shared.mysql.Empresas;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.validaciones.Validaciones;
import es.serversurvival._shared.utils.Funciones;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.DARK_RED;

@Command("empresas editardescripccion")
public class EditarDescComando extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas editardescripccion <empresa> <nueva desc>";
    private EditarDescUseCase useCase = EditarDescUseCase.INSTANCE;

    @Override
    public void execute(CommandSender player, String[] args) {
        ValidationResult result = ValidatorService.startValidating(args.length >= 3, Validaciones.True.of(usoIncorrecto))
                .andMayThrowException(() -> Funciones.buildStringFromArray(args, 2), usoIncorrecto, Validaciones.MaxLength.of(Empresas.CrearEmpresaDescLonMax, "La descripccino no puede ser tan larga"))
                .andMayThrowException(() -> args[1], usoIncorrecto, Validaciones.OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if (result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        String nombreEmpresa = args[1];
        String descripccion = Funciones.buildStringFromArray(args, 2);

        useCase.edit(nombreEmpresa, descripccion);

        player.sendMessage(ChatColor.GOLD + "Has cambiado la descripccion de tu empresa: " + ChatColor.DARK_AQUA + nombreEmpresa +
                ChatColor.GOLD + " a ver en " + ChatColor.AQUA + "/empresas vertodas");
    }
}
