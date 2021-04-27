package es.serversurvival.nfs.empresas.vender;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.nfs.shared.comandos.PixelcoinCommand;
import es.serversurvival.nfs.utils.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.DARK_RED;

@Command("empresas vender")
public class VenderEmpresaComando extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas vender <empresa> <jugador> <precio>";

    @Override
    public void execute(CommandSender player, String[] args) {
        ValidationResult result = ValidationsService.startValidating(args.length == 4, Validaciones.True.of(usoIncorrecto))
                .andMayThrowException(() -> args[2], usoIncorrecto, Validaciones.JugadorOnline, Validaciones.NotEqualsIgnoreCase.of(player.getName(), "No te lo puedes vender a ti mismo"))
                .andMayThrowException(() -> args[3], usoIncorrecto, Validaciones.PositiveNumber, Validaciones.SuficientesPixelcoins.of(() -> args[2], "No tiene tantas pixelcoins como crees xdd"))
                .andMayThrowException(() -> args[1], usoIncorrecto, Validaciones.OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        VenderSolicitud venderSolicitud = new VenderSolicitud(player.getName(), args[2], args[1], Double.parseDouble(args[3]));
        venderSolicitud.enviarSolicitud();
    }
}
