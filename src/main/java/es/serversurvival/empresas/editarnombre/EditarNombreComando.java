package es.serversurvival.empresas.editarnombre;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.empresas._shared.mysql.Empresas;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.validaciones.Validaciones;
import es.serversurvival._shared.utils.Funciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.DARK_RED;

@Command("empresas editarnombre")
public class EditarNombreComando extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas editarnombre <empresa> <nuevo nombre>";
    private final EditarNombreUseCase useCase = EditarNombreUseCase.INSTANCE;

    @Override
    public void execute(CommandSender player, String[] args) {
        ValidationResult result = ValidationsService.startValidating(args.length == 3, Validaciones.True.of(usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, Validaciones.OwnerDeEmpresa.of(player.getName()), Validaciones.MaxLength.of(Empresas.CrearEmpresaNombreLonMax, "El nombre no puede ser tan largo"))
                .andMayThrowException(() -> args[2], usoIncorrecto, Validaciones.NombreEmpresaNoPillado)
                .validateAll();

        if(result.isFailed()) {
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        String antiguoNombre = args[1];
        String nuevoNombre = args[2];

        useCase.editar(antiguoNombre, nuevoNombre);

        Funciones.enviarMensajeYSonido((Player) player, ChatColor.GOLD + "Has cambiado de nombre a tu empresa!", Sound.ENTITY_PLAYER_LEVELUP);
    }
}
