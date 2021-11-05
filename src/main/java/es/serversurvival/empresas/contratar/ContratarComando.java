package es.serversurvival.empresas.contratar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.empleados._shared.mysql.TipoSueldo;
import es.serversurvival.shared.comandos.PixelcoinCommand;
import es.serversurvival.shared.utils.validaciones.Validaciones;
import es.serversurvival.empleados.contratar.ContratarSolicitud;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.DARK_RED;

@Command("empresas contratar")
public class ContratarComando extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas contratar <jugador> <empresa> <sueldo> <tipo sueldo (/ayuda empresas)> [cargo]";

    @Override
    public void execute(CommandSender player, String[] args) {
        ValidationResult result = ValidationsService.startValidating(args.length == 5 || args.length == 6, Validaciones.True.of(usoIncorrecto))
                .andMayThrowException(() -> args[3], usoIncorrecto, Validaciones.NaturalNumber)
                .andMayThrowException(() -> args[1], usoIncorrecto, Validaciones.JugadorOnline, Validaciones.NoLeHanEnviadoSolicitud, Validaciones.NoTrabajaEmpresa.en(() -> args[2]), Validaciones.NotEqualsIgnoreCase.of(player.getName(), "No te puedes contratar a ti mismo"))
                .andMayThrowException(() -> TipoSueldo.codigoCorrecto(args[4]), usoIncorrecto, Validaciones.True.of("El tipo de sueldo solo puede ser d: cdda dia, s: cada semana, 2s: cada dos semanas, m: cada mes"))
                .andMayThrowException(() -> args[2], usoIncorrecto, Validaciones.OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        Player jugadorAContratarPlayer = Bukkit.getPlayer(args[1]);
        double sueldo = Double.parseDouble(args[3]);
        String cargo;
        if(args.length == 5){
            cargo = args[4];
        }else{
            cargo = "Trabajador";
        }

        TipoSueldo tipoSueldo = TipoSueldo.ofCodigo(args[4]);

        ContratarSolicitud solicitud = new ContratarSolicitud(player.getName(), jugadorAContratarPlayer.getName(), args[2], sueldo, tipoSueldo, cargo);
        solicitud.enviarSolicitud();
    }
}
