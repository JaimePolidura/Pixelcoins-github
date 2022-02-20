package es.serversurvival.empresas.contratar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerArgs;
import es.serversurvival.empleados._shared.mysql.TipoSueldo;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.validaciones.Validaciones;
import es.serversurvival.empleados.contratar.ContratarSolicitud;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(
        value = "empresas contratar",
        isSubCommand = true,
        args = {"jugador", "empresa", "sueldo", "tipoSueldo", "[cargo]¡trabajador!"}
)
public class ContratarComandoExecutor extends PixelcoinCommand implements CommandRunnerArgs<ContratarComando> {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas contratar <jugador> <empresa> <sueldo> <tipo sueldo (/ayuda empresas)> [cargo]";

    @Override
    public void execute(ContratarComando contratarComando, CommandSender player) {
        String jugador = contratarComando.getJugador();
        String empresa = contratarComando.getEmpresa();
        double sueldo = contratarComando.getSueldo();
        String tipoSueldoString = contratarComando.getTipoSueldo();
        String cargo = contratarComando.getCargo();

        ValidationResult result = ValidatorService
                .startValidating(sueldo, NaturalNumber)
                .and(jugador, JugadorOnline, NoLeHanEnviadoSolicitud, NoTrabajaEmpresa.en(empresa), NotEqualsIgnoreCase.of(player.getName(), "No te puedes contratar a ti mismo"))
                .and(TipoSueldo.codigoCorrecto(tipoSueldoString), True.of("El tipo de sueldo solo puede ser d: cdda dia, s: cada semana, 2s: cada dos semanas, m: cada mes"))
                .and(empresa, OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }


        TipoSueldo tipoSueldo = TipoSueldo.ofCodigo(tipoSueldoString);

        ContratarSolicitud solicitud = new ContratarSolicitud(player.getName(), jugador, empresa, sueldo, tipoSueldo, cargo);
    }
}
