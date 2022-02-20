package es.serversurvival.empresas.vender;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(value = "empresas vender", isSubCommand = true, args = {"empresa", "jugador", "precio"})
public class VenderEmpresaComandoExecutor extends PixelcoinCommand implements CommandRunnerArgs<VenderEmpresaComando> {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas vender <empresa> <jugador> <precio>";

    @Override
    public void execute(VenderEmpresaComando comando, CommandSender player) {
        String empresa = comando.getEmpresa();
        String jugador = comando.getJugador();
        double preico = comando.getPrecio();

        ValidationResult result = ValidatorService
                .startValidating(jugador, JugadorOnline, NotEqualsIgnoreCase.of(player.getName(), "No te lo puedes vender a ti mismo"))
                .and(preico, PositiveNumber, SuficientesPixelcoins.of(jugador, "No tiene tantas pixelcoins como crees xdd"))
                .and(empresa, OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        VenderSolicitud venderSolicitud = new VenderSolicitud(player.getName(), jugador, empresa, preico);
        venderSolicitud.enviarSolicitud();
    }
}
